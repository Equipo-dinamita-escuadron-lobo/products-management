package com.products_management.application.service;

import com.products_management.application.ports.input.IProductExportUseCase;
import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.exception.ErrorCode;
import com.products_management.domain.exception.product.ExcelValidationException;
import com.products_management.domain.exception.product.ProductExportException;
import com.products_management.domain.model.Category;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.ProductType;
import com.products_management.domain.model.UnitOfMeasure;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Servicio para exportar productos en formato Excel.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductExportService implements IProductExportUseCase {

    private final IProductPersistencePort productPersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IProductTypePersistencePort productTypePersistencePort;
    private final IUnitOfMeasurePersistencePort unitOfMeasurePersistencePort;
    private final ProductExcelValidationService excelValidationService;

   
    private static final int EXPORT_PAGE_SIZE = 1000;
    private static final String SELECT_PLACEHOLDER = "Seleccionar...";

    /**
     * Obtiene productos filtrados aplicando el filtro en la base de datos.
     * Utiliza paginación automática para exportar TODOS los registros sin límite,
     * optimizando el uso de memoria mediante procesamiento por lotes.
     *
     * @param entId ID de la empresa
     * @param status Estado de los productos (true=activos, false=inactivos, null=todos)
     * @return lista completa de productos filtrados
     */
    private List<Product> getFilteredProducts(String entId, Boolean status) {
        List<Product> allProducts = new ArrayList<>();
        int currentPage = 0;
        Page<Product> page;

        do {
            // Crear pageable para la página actual
            Pageable pageable = PageRequest.of(currentPage, EXPORT_PAGE_SIZE);

            // Obtener página según filtros usando el método disponible
            if (status != null) {
                // Para filtrar por estado, usamos findActivatedWithPagination si status es true
                if (status) {
                    page = productPersistencePort.findActivatedWithPagination(entId, currentPage, EXPORT_PAGE_SIZE);
                } else {
                    // Para inactivos, necesitamos una lógica diferente o usar el método general con filtro
                    page = productPersistencePort.findByEnterpriseIdWithFilters(entId, null, currentPage, EXPORT_PAGE_SIZE, "name", "asc");
                    // Filtrar manualmente por estado inactivo
                    List<Product> filteredContent = page.getContent().stream()
                            .filter(product -> !product.isState())
                            .toList();
                    page = new PageImpl<>(filteredContent, pageable, filteredContent.size());
                }
            } else {
                page = productPersistencePort.findByEnterpriseIdWithFilters(entId, null, currentPage, EXPORT_PAGE_SIZE, "name", "asc");
            }

            // Agregar contenido de esta página a la lista total
            if (page != null && page.hasContent()) {
                allProducts.addAll(page.getContent());
            }

            currentPage++;

        } while (page != null && page.hasNext());

        return allProducts;
    }    private CellStyle createHeaderStyle(Workbook workbook) {
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

    /**
     * Crea estilo para encabezados de columnas opcionales (fondo gris claro).
     */
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

        // Ajustar altura de la fila de encabezados para mostrar múltiples líneas
        headerRow.setHeightInPoints(35);
    }

    private void createHeaderCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    private void fillData(Sheet sheet, List<Product> products, CellStyle dataStyle, String entId) {
        int rowIndex = 1;

        for (Product product : products) {
            Row row = sheet.createRow(rowIndex++);
            int colIndex = 0;

            // Datos requeridos
            createDataCell(row, colIndex++, product.getCode(), dataStyle);
            createDataCell(row, colIndex++, product.getName(), dataStyle);
            createDataCell(row, colIndex++, product.getReference() != null ? product.getReference() : "", dataStyle);
            createDataCell(row, colIndex++, product.getPresentation() != null ? product.getPresentation() : "", dataStyle);
            createDataCell(row, colIndex++, product.getDescription(), dataStyle);

            createDataCell(row, colIndex++, product.getCost(), dataStyle);
            createDataCell(row, colIndex++, product.getQuantity(), dataStyle);
            createDataCell(row, colIndex++, getUnitOfMeasureName(product.getUnitOfMeasureId(), entId), dataStyle);
            createDataCell(row, colIndex++, getCategoryName(product.getCategoryId(), entId), dataStyle);
            createDataCell(row, colIndex++, getProductTypeName(product.getProductTypeId(), entId), dataStyle);
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

    private String getUnitOfMeasureName(Long unitOfMeasureId, String entId) {
        if (unitOfMeasureId == null) return "";
        return unitOfMeasurePersistencePort.findByIdAndEnterpriseId(unitOfMeasureId, entId)
                .map(UnitOfMeasure::getName)
                .orElse("");
    }

    private String getCategoryName(Long categoryId, String entId) {
        if (categoryId == null) return "";
        return categoryPersistencePort.findByIdAndEnterpriseId(categoryId, entId)
                .map(Category::getName)
                .orElse("");
    }

    private String getProductTypeName(Long productTypeId, String entId) {
        if (productTypeId == null) return "";
        return productTypePersistencePort.findByIdAndEnterpriseId(productTypeId, entId)
                .map(ProductType::getName)
                .orElse("");
    }

    private void autoSizeColumns(Sheet sheet) {
        for (int i = 0; i < 11; i++) {
            // Primero aplicar el auto-sizing basado en el contenido
            sheet.autoSizeColumn(i);

            // Obtener el ancho calculado automáticamente
            int autoWidth = sheet.getColumnWidth(i);

            // Establecer límites razonables
            int minWidth = 1500; // Ancho mínimo más pequeño
            int maxWidth = 25000; // Ancho máximo más generoso

            // Aplicar los límites
            if (autoWidth < minWidth) {
                sheet.setColumnWidth(i, minWidth);
            } else if (autoWidth > maxWidth) {
                sheet.setColumnWidth(i, maxWidth);
            }

            // Agregar un pequeño padding al ancho calculado para mejor legibilidad
            int finalWidth = Math.min(Math.max(autoWidth + 500, minWidth), maxWidth);
            sheet.setColumnWidth(i, finalWidth);
        }
    }



    /**
     * Exporta una plantilla de productos con validaciones de datos (listas desplegables).
     */
    @Override
    public Resource exportProductTemplateWithValidations(String entId) {
        try {
            byte[] templateData = generateTemplateWithValidations(entId);
            return new ByteArrayResource(templateData);
        } catch (Exception e) {
            throw new ExcelValidationException(ErrorCode.EXCEL_VALIDATION_ERROR,
                    "Error al generar plantilla", e);
        }
    }

    /**
     * Exporta productos existentes con validaciones de datos (listas desplegables).
     */
    @Override
    public Resource exportProductsWithValidations(String entId, Boolean status) {
        try {
            // Obtener datos según filtros
            List<Product> products = getFilteredProducts(entId, status);

            // Validar que existan productos para exportar
            if (products.isEmpty()) {
                throw ProductExportException.forNoData(status);
            }

            // Generar archivo Excel con datos y validaciones
            byte[] excelData = generateExcelFileWithValidations(products, entId);

            return new ByteArrayResource(excelData);

        } catch (ProductExportException e) {
            throw e;
        } catch (Exception e) {
            throw new ExcelValidationException(ErrorCode.EXCEL_VALIDATION_ERROR,
                    "Error al generar archivo de exportación", e);
        }
    }

    private byte[] generateTemplateWithValidations(String entId) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Plantilla_Productos");

            // Crear estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle templateStyle = createTemplateStyle(workbook);

            createHeaders(sheet, headerStyle, createOptionalHeaderStyle(workbook));

            // Crear filas de ejemplo con estilos
            createTemplateRows(sheet, templateStyle, 10); // 10 filas de ejemplo

            // Aplicar validaciones de datos
            applyValidationsToTemplate(sheet, entId);

            // Ajustar ancho de columnas
            autoSizeColumns(sheet);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private CellStyle createTemplateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        style.setFont(font);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    private void createTemplateRows(Sheet sheet, CellStyle templateStyle, int numberOfRows) {
        for (int i = 1; i <= numberOfRows; i++) {
            Row row = sheet.createRow(i);
            int colIndex = 0;

            if (i == 1) {
                // Primera fila con indicadores (estilo normal)
                createHeaderCell(row, colIndex++, "", templateStyle);
                createHeaderCell(row, colIndex++, "Producto de Ejemplo", templateStyle);
                createHeaderCell(row, colIndex++, "REF001", templateStyle);
                createHeaderCell(row, colIndex++, "Caja x 12", templateStyle);
                createHeaderCell(row, colIndex++, "Descripción del producto", templateStyle);
                createHeaderCell(row, colIndex++, "0", templateStyle); // Costo
                createHeaderCell(row, colIndex++, "0", templateStyle); // Cantidad
                createHeaderCell(row, colIndex++, SELECT_PLACEHOLDER, templateStyle); // Unidad de Medida
                createHeaderCell(row, colIndex++, SELECT_PLACEHOLDER, templateStyle); // Categoría
                createHeaderCell(row, colIndex++, SELECT_PLACEHOLDER, templateStyle); // Tipo de Producto
                createHeaderCell(row, colIndex++, "", templateStyle); // Estado
            } else {
                // Filas adicionales vacías con el mismo estilo
                createEmptyTemplateRow(row, templateStyle);
            }
        }
    }

    private void createEmptyTemplateRow(Row row, CellStyle templateStyle) {
        for (int i = 0; i < 11; i++) {
            createHeaderCell(row, i, "", templateStyle);
        }
    }



    private void applyValidationsToTemplate(Sheet sheet, String entId) {
        int startRow = 1; // Después del encabezado
        int endRow = 1000; // Permitir muchas filas para la plantilla

        excelValidationService.applyProductValidations(sheet, entId, startRow, endRow);
    }

    /**
     * Genera archivo Excel con datos reales y validaciones aplicadas.
     */
    private byte[] generateExcelFileWithValidations(List<Product> products, String entId) throws IOException {
        try (Workbook workbook = new XSSFWorkbook();
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            Sheet sheet = workbook.createSheet("Productos");

            // Crear estilos
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            createHeaders(sheet, headerStyle, createOptionalHeaderStyle(workbook));

            fillData(sheet, products, dataStyle, entId);

            applyValidationsToDataSheet(sheet, entId, products.size());

            autoSizeColumns(sheet);

            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    /**
     * Aplica validaciones de datos a una hoja con datos existentes.
     */
    private void applyValidationsToDataSheet(Sheet sheet, String entId, int dataRowCount) {
        int startRow = 1; // Después del encabezado
        int endRow = Math.max(dataRowCount + 100, 1000); // Datos existentes + filas adicionales

        excelValidationService.applyProductValidations(sheet, entId, startRow, endRow);
    }
}