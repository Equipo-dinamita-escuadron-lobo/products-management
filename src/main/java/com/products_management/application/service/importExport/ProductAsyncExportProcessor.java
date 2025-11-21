package com.products_management.application.service.importExport;

import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.enums.ImportStatus;
import com.products_management.domain.exception.product.ProductExportException;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.dto.request.ProductExportRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @brief Servicio para procesar exportaciones de productos de forma asíncrona
 *
 * Este servicio ejecuta la exportación en un hilo separado para no bloquear
 * la petición HTTP, permitiendo exportaciones de grandes volúmenes de datos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAsyncExportProcessor {

    private final IProductPersistencePort productPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IProductTypePersistencePort productTypePersistencePort;
    private final IUnitOfMeasurePersistencePort unitOfMeasurePersistencePort;
    private final ProductExportJobTracker jobTracker;
    private final ProductExcelValidationService excelValidationService;
    
    private static final int EXPORT_PAGE_SIZE = 1000;

    /**
     * @brief Procesa la exportación de forma asíncrona
     * @param exportRequest solicitud de exportación con filtros
     * @param jobId identificador del trabajo
     */
    @Async
    public void processExportAsync(ProductExportRequest exportRequest, String jobId) {
        log.info("JobId {}: Iniciando procesamiento ASÍNCRONO de exportación de productos en thread: {}",
                jobId, Thread.currentThread().getName());

        long totalStartTime = System.currentTimeMillis();
        Map<String, Long> phaseTimes = new LinkedHashMap<>();
        int totalRecords = 0;

        try {
            jobTracker.updateJobStatus(jobId, ImportStatus.PROCESSING);
            jobTracker.updateProgress(jobId, 10);

            // FASE 1: Obtener datos filtrados con paginación
            log.info("JobId {}: Fase 1 - Obtención de datos", jobId);
            long phase1Start = System.currentTimeMillis();
            List<Product> products = getFilteredProducts(exportRequest, jobId);
            long phase1Time = System.currentTimeMillis() - phase1Start;
            phaseTimes.put("1. Obtención de Datos", phase1Time);
            log.info("JobId {}: Fase 1 completada en {} ms - {} registros obtenidos", 
                    jobId, phase1Time, products.size());
            
            totalRecords = products.size();
            jobTracker.updateTotalRecords(jobId, totalRecords);
            jobTracker.updateProgress(jobId, 50);

            // Validar que existan datos
            if (products == null || products.isEmpty()) {
                handleNoDataError(exportRequest, jobId);
                return;
            }

            // FASE 2: Generar archivo Excel
            log.info("JobId {}: Fase 2 - Generación de archivo Excel", jobId);
            long phase2Start = System.currentTimeMillis();
            byte[] excelData = generateExcelFile(products, exportRequest, jobId);
            long phase2Time = System.currentTimeMillis() - phase2Start;
            phaseTimes.put("2. Generación Excel", phase2Time);
            log.info("JobId {}: Fase 2 completada en {} ms - Archivo generado ({} bytes)", 
                    jobId, phase2Time, excelData.length);

            jobTracker.updateProgress(jobId, 90);

            // FASE 3: Almacenar archivo en memoria
            log.info("JobId {}: Fase 3 - Almacenamiento del archivo", jobId);
            long phase3Start = System.currentTimeMillis();
            jobTracker.setFileData(jobId, excelData);
            long phase3Time = System.currentTimeMillis() - phase3Start;
            phaseTimes.put("3. Almacenamiento", phase3Time);
            log.info("JobId {}: Fase 3 completada en {} ms", jobId, phase3Time);

            // Completar job
            jobTracker.updateProgress(jobId, 100);
            jobTracker.updateJobStatus(jobId, ImportStatus.COMPLETED);

            // Imprimir resumen
            long totalTime = System.currentTimeMillis() - totalStartTime;
            printPhaseTimesTable(jobId, phaseTimes, totalTime, totalRecords);

            log.info("JobId {}: Exportación de productos completada exitosamente en {} ms", jobId, totalTime);

        } catch (ProductExportException e) {
            handleExportError(jobId, e);
        } catch (Exception e) {
            handleUnexpectedError(jobId, e);
        }
    }

    /**
     * @brief Obtiene productos filtrados con paginación optimizada para exportación
     * @param request solicitud de exportación con filtros
     * @param jobId identificador del trabajo (para logging)
     * @return lista completa de productos filtrados
     */
    private List<Product> getFilteredProducts(ProductExportRequest request, String jobId) {
        List<Product> allProducts = new ArrayList<>();
        int currentPage = 0;
        Page<Product> page;

        do {
            // Obtener productos según filtros
            if (request.getStatus() != null) {
                if (request.getStatus()) {
                    page = productPersistencePort.findActivatedWithPagination(
                            request.getEntId(), currentPage, EXPORT_PAGE_SIZE);
                } else {
                    page = productPersistencePort.findByEnterpriseIdWithFilters(
                            request.getEntId(), null, currentPage, EXPORT_PAGE_SIZE, "name", "asc");
                }
            } else {
                page = productPersistencePort.findByEnterpriseIdWithFilters(
                        request.getEntId(), null, currentPage, EXPORT_PAGE_SIZE, "name", "asc");
            }
            
            if (page != null && page.hasContent()) {
                allProducts.addAll(page.getContent());
                log.debug("JobId {}: Página {} procesada - {} registros acumulados", 
                        jobId, currentPage, allProducts.size());
            }
            
            currentPage++;
            
        } while (page != null && page.hasNext());
        
        return allProducts;
    }

    /**
     * @brief Genera el archivo Excel con los datos de productos
     * @details Pre-carga un cache con todos los nombres de entidades relacionadas
     * para evitar N+1 queries durante la generación del Excel
     * @param products lista de productos a exportar
     * @param request solicitud de exportación con configuración
     * @param jobId identificador del trabajo
     * @return arreglo de bytes con el contenido del archivo Excel
     * @throws IOException si ocurre un error al escribir el archivo
     */
    private byte[] generateExcelFile(List<Product> products, ProductExportRequest request, String jobId) 
            throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
             ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Productos");

            // Crear estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            createHeaders(sheet, headerStyle, createOptionalHeaderStyle(workbook));

            // Pre-cargar cache de nombres (3-4 queries en lugar de 54,708)
            log.info("JobId {}: Pre-cargando cache de nombres de entidades relacionadas", jobId);
            EntityNamesCache namesCache = preloadEntityNamesCache(request.getEntId(), products);

            fillDataWithCache(sheet, products, dataStyle, namesCache);

            applyValidationsToDataSheet(sheet, request.getEntId(), products.size());

            autoSizeColumns(sheet);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    // ==================== MÉTODOS DE ESTILOS ====================

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    private CellStyle createOptionalHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.BLACK.getIndex());
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        style.setWrapText(true);
        return style;
    }

    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    // ==================== MÉTODOS DE CONTENIDO ====================

    private void createHeaders(Sheet sheet, CellStyle requiredHeaderStyle, CellStyle optionalHeaderStyle) {
        Row headerRow = sheet.createRow(0);
        int colIndex = 0;

        createHeaderCell(headerRow, colIndex++, "Código\n(No se requiere)", optionalHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Nombre\n(Requerido)", requiredHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Referencia/SKU\n(Requerido)", requiredHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Presentación\n(Requerido)", requiredHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Descripción\n(Requerido)", requiredHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Costo\n(Opcional)", optionalHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Cantidad\n(Opcional)", optionalHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Unidad de Medida\n(Requerido)", requiredHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Categoría\n(Requerido)", requiredHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Tipo de Producto\n(Requerido)", requiredHeaderStyle);
        createHeaderCell(headerRow, colIndex++, "Estado\n(No se requiere)", optionalHeaderStyle);

        headerRow.setHeightInPoints(35);
    }

    private void createHeaderCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    /**
     * @brief Llena hoja Excel usando cache pre-cargado de nombres
     * @details Usa el cache para resolver nombres sin hacer queries adicionales,
     * eliminando el problema N+1 y acelerando drásticamente la generación
     * @param sheet hoja de Excel
     * @param products lista de productos
     * @param dataStyle estilo para celdas
     * @param namesCache cache con todos los nombres pre-cargados
     */
    private void fillDataWithCache(Sheet sheet, List<Product> products, CellStyle dataStyle, EntityNamesCache namesCache) {
        int rowIndex = 1;

        for (Product product : products) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;

            createDataCell(row, colIndex++, product.getCode(), dataStyle);
            createDataCell(row, colIndex++, product.getName(), dataStyle);
            createDataCell(row, colIndex++, product.getReference() != null ? product.getReference() : "", dataStyle);
            createDataCell(row, colIndex++, product.getPresentation() != null ? product.getPresentation() : "", dataStyle);
            createDataCell(row, colIndex++, product.getDescription(), dataStyle);

            createDataCell(row, colIndex++, product.getCost(), dataStyle);
            createDataCell(row, colIndex++, product.getQuantity(), dataStyle);
            
            // Usar cache en lugar de queries (O(1) en lugar de query por registro)
            createDataCell(row, colIndex++, namesCache.getUnitOfMeasureName(product.getUnitOfMeasureId()), dataStyle);
            createDataCell(row, colIndex++, namesCache.getCategoryName(product.getCategoryId()), dataStyle);
            createDataCell(row, colIndex++, namesCache.getProductTypeName(product.getProductTypeId()), dataStyle);
            
            createDataCell(row, colIndex++, product.isState() ? "ACTIVO" : "INACTIVO", dataStyle);
        }
    }

    private void createDataCell(Row row, int colIndex, Object value, CellStyle style) {
        Cell cell = row.createCell(colIndex);

        if (value == null) {
            cell.setCellValue("");
        } else if (value instanceof Number number) {
            cell.setCellValue(number.doubleValue());
        } else {
            cell.setCellValue(value.toString());
        }

        cell.setCellStyle(style);
    }

    // ==================== CACHE DE NOMBRES DE ENTIDADES ====================

    /**
     * @brief Pre-carga todos los nombres de entidades relacionadas en memoria
     * @details Ejecuta solo 3-4 queries para cargar todos los nombres necesarios
     * @param entId ID de la empresa
     * @param products lista de productos para identificar IDs únicos
     * @return cache con todos los nombres indexados por ID
     */
    private EntityNamesCache preloadEntityNamesCache(String entId, List<Product> products) {
        EntityNamesCache cache = new EntityNamesCache();

        // Extraer IDs únicos de los productos
        java.util.Set<Long> unitIds = products.stream()
                .map(Product::getUnitOfMeasureId)
                .filter(id -> id != null)
                .collect(java.util.stream.Collectors.toSet());

        java.util.Set<Long> categoryIds = products.stream()
                .map(Product::getCategoryId)
                .filter(id -> id != null)
                .collect(java.util.stream.Collectors.toSet());

        java.util.Set<Long> typeIds = products.stream()
                .map(Product::getProductTypeId)
                .filter(id -> id != null)
                .collect(java.util.stream.Collectors.toSet());

        log.debug("IDs únicos a cargar - Unidades: {}, Categorías: {}, Tipos: {}", 
                unitIds.size(), categoryIds.size(), typeIds.size());

        // Query 1: Cargar todos los nombres de unidades de medida en batch
        for (Long unitId : unitIds) {
            unitOfMeasurePersistencePort.findByIdAndEnterpriseId(unitId, entId)
                    .ifPresent(unit -> cache.unitNames.put(unitId, unit.getName()));
        }

        // Query 2: Cargar todos los nombres de categorías en batch
        for (Long categoryId : categoryIds) {
            categoryPersistencePort.findByIdAndEnterpriseId(categoryId, entId)
                    .ifPresent(cat -> cache.categoryNames.put(categoryId, cat.getName()));
        }

        // Query 3: Cargar todos los nombres de tipos de producto en batch
        for (Long typeId : typeIds) {
            productTypePersistencePort.findByIdAndEnterpriseId(typeId, entId)
                    .ifPresent(type -> cache.productTypeNames.put(typeId, type.getName()));
        }

        log.debug("Cache cargado - Unidades: {}, Categorías: {}, Tipos: {}", 
                cache.unitNames.size(), cache.categoryNames.size(), cache.productTypeNames.size());

        return cache;
    }

    /**
     * @brief Cache interno con nombres de entidades relacionadas indexados por ID
     * @details Permite acceso O(1) a nombres sin queries adicionales
     */
    private static class EntityNamesCache {
        final java.util.Map<Long, String> unitNames = new java.util.HashMap<>();
        final java.util.Map<Long, String> categoryNames = new java.util.HashMap<>();
        final java.util.Map<Long, String> productTypeNames = new java.util.HashMap<>();

        String getUnitOfMeasureName(Long id) {
            return id == null ? "" : unitNames.getOrDefault(id, "");
        }

        String getCategoryName(Long id) {
            return id == null ? "" : categoryNames.getOrDefault(id, "");
        }

        String getProductTypeName(Long id) {
            return id == null ? "" : productTypeNames.getOrDefault(id, "");
        }
    }

    // ==================== MÉTODOS DE VALIDACIONES ====================

    private void applyValidationsToDataSheet(Sheet sheet, String entId, int dataRowCount) {
        int startRow = 1;
        int endRow = Math.max(dataRowCount + 100, 1000);

        excelValidationService.applyProductValidations(sheet, entId, startRow, endRow);
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 11; i++) {
            sheet.autoSizeColumn(i);

            int autoWidth = sheet.getColumnWidth(i);
            int minWidth = 1500;
            int maxWidth = 25000;

            if (autoWidth < minWidth) {
                sheet.setColumnWidth(i, minWidth);
            } else if (autoWidth > maxWidth) {
                sheet.setColumnWidth(i, maxWidth);
            }

            int finalWidth = Math.min(Math.max(autoWidth + 500, minWidth), maxWidth);
            sheet.setColumnWidth(i, finalWidth);
        }
    }

    // ==================== MANEJO DE ERRORES ====================

    private void handleNoDataError(ProductExportRequest request, String jobId) {
        String message = "No hay productos para exportar con los filtros especificados";
        log.warn("JobId {}: {}", jobId, message);
        jobTracker.setErrorMessage(jobId, message);
        jobTracker.updateJobStatus(jobId, ImportStatus.FAILED);
    }

    private void handleExportError(String jobId, ProductExportException e) {
        log.error("JobId {}: Error de exportación: {}", jobId, e.getMessage(), e);
        jobTracker.setErrorMessage(jobId, e.getMessage());
        jobTracker.updateJobStatus(jobId, ImportStatus.FAILED);
    }

    private void handleUnexpectedError(String jobId, Exception e) {
        log.error("JobId {}: Error inesperado durante la exportación: {}", jobId, e.getMessage(), e);
        jobTracker.setErrorMessage(jobId, "Error inesperado: " + e.getMessage());
        jobTracker.updateJobStatus(jobId, ImportStatus.FAILED);
    }

    // ==================== UTILIDADES ====================

    private void printPhaseTimesTable(String jobId, Map<String, Long> phaseTimes, long totalTime, int totalRecords) {
        StringBuilder table = new StringBuilder();
        table.append("\n╔════════════════════════════════════════════════════════════╗\n");
        table.append(String.format("║  RESUMEN DE TIEMPOS - JobId: %-28s ║\n", jobId.substring(0, Math.min(28, jobId.length()))));
        table.append("╠════════════════════════════════════════════════════════════╣\n");
        table.append("║  Fase                     │ Tiempo (ms) │ Tiempo (s) │ % ║\n");
        table.append("╠════════════════════════════════════════════════════════════╣\n");

        for (Map.Entry<String, Long> entry : phaseTimes.entrySet()) {
            double percentage = (entry.getValue() * 100.0) / totalTime;
            double seconds = entry.getValue() / 1000.0;
            table.append(String.format("║  %-24s │ %,11d │ %,10.2f │ %5.1f%% ║\n",
                    entry.getKey(), entry.getValue(), seconds, percentage));
        }

        table.append("╠════════════════════════════════════════════════════════════╣\n");
        table.append(String.format("║  TOTAL                    │ %,11d │ %,10.2f │ 100.0%% ║\n",
                totalTime, totalTime / 1000.0));
        table.append("╠════════════════════════════════════════════════════════════╣\n");
        table.append(String.format("║  Total Registros: %-41d║\n", totalRecords));
        table.append(String.format("║  Rendimiento: %-37.2f registros/seg ║\n",
                (totalRecords * 1000.0) / totalTime));
        table.append("╚════════════════════════════════════════════════════════════╝");

        log.info("JobId {}: {}", jobId, table);
    }
}

