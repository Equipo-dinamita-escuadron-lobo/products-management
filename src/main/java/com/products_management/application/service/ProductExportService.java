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
 * @brief Servicio para exportar productos en formato Excel
 *
 * Maneja la exportación de productos existentes y generación de plantillas Excel
 * con validaciones de datos integradas. Implementa paginación automática para
 * grandes volúmenes de datos y resolución de entidades relacionadas.
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
     * @brief Obtiene productos filtrados con paginación automática por lotes
     *
     * Realiza la obtención completa de productos aplicando filtros de estado y
     * utilizando paginación automática para manejar grandes volúmenes de datos
     * de manera eficiente sin sobrecargar la memoria.
     *
     * @param entId ID de la empresa para filtrar productos
     * @param status Estado de los productos (true=activos, false=inactivos, null=todos)
     * @return Lista completa de productos filtrados de todos los lotes
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
    }    /**
     * @brief Crea estilo para encabezados con fondo azul oscuro y texto blanco
     *
     * Configura un estilo visual profesional para encabezados de columnas con
     * fondo azul oscuro, texto blanco en negrita, bordes delgados y alineación centrada
     * para mejorar la legibilidad y apariencia profesional del documento Excel.
     *
     * @param workbook Libro de trabajo Excel donde crear el estilo
     * @return Estilo configurado para encabezados de columnas
     */
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

    /**
     * @brief Crea estilo para encabezados opcionales con fondo gris claro
     *
     * Configura un estilo visual distintivo para encabezados de campos opcionales
     * con fondo gris claro, texto negro en negrita y bordes delgados. Este estilo
     * ayuda a diferenciar visualmente los campos obligatorios de los opcionales.
     *
     * @param workbook Libro de trabajo Excel donde crear el estilo
     * @return Estilo configurado para encabezados de campos opcionales
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

    /**
     * @brief Crea estilo para celdas de datos con bordes delgados
     *
     * Configura un estilo básico para celdas de datos con bordes delgados
     * y alineación vertical centrada para mantener consistencia visual
     * en todas las celdas de contenido del documento Excel.
     *
     * @param workbook Libro de trabajo Excel donde crear el estilo
     * @return Estilo configurado para celdas de datos
     */
    private CellStyle createDataStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        return style;
    }

    /**
     * @brief Crea fila de encabezados con estilos diferenciados para campos requeridos/opcionales
     *
     * Genera la fila de encabezados completa del Excel con 11 columnas específicas
     * para productos, aplicando estilos visuales diferenciados para distinguir
     * campos obligatorios (azul oscuro) de opcionales (gris claro). Incluye
     * indicadores textuales de requerimiento en cada encabezado.
     *
     * @param sheet Hoja de trabajo Excel donde crear los encabezados
     * @param requiredHeaderStyle Estilo para encabezados de campos requeridos
     * @param optionalHeaderStyle Estilo para encabezados de campos opcionales
     */
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

    /**
     * @brief Crea celda de encabezado con valor y estilo especificados
     *
     * Método auxiliar para crear celdas de encabezado de manera consistente,
     * aplicando el valor de texto y el estilo visual correspondiente a cada
     * celda de encabezado en la fila de títulos.
     *
     * @param row Fila donde crear la celda de encabezado
     * @param colIndex Índice de columna donde ubicar la celda
     * @param value Valor de texto para el encabezado
     * @param style Estilo visual a aplicar a la celda
     */
    private void createHeaderCell(Row row, int colIndex, String value, CellStyle style) {
        Cell cell = row.createCell(colIndex);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }

    /**
     * @brief Llena hoja Excel con datos de productos y nombres de entidades relacionadas
     *
     * Pobla la hoja Excel con todos los datos de productos, convirtiendo IDs de entidades
     * relacionadas (categorías, tipos de producto, unidades de medida) a sus nombres
     * legibles. Maneja valores nulos y formatos de estado para presentación amigable.
     *
     * @param sheet Hoja de trabajo Excel donde insertar los datos
     * @param products Lista de productos a exportar
     * @param dataStyle Estilo visual para las celdas de datos
     * @param entId ID de la empresa para resolver nombres de entidades relacionadas
     */
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

    /**
     * @brief Crea celda de datos manejando diferentes tipos de valores (String, Number, etc.)
     *
     * Método auxiliar que crea celdas de datos con manejo inteligente de tipos:
     * convierte números a formato numérico Excel, strings a texto y maneja valores
     * null convirtiéndolos a strings vacías. Aplica el estilo visual especificado.
     *
     * @param row Fila donde crear la celda de datos
     * @param colIndex Índice de columna donde ubicar la celda
     * @param value Valor a insertar (maneja null, números y strings automáticamente)
     * @param style Estilo visual a aplicar a la celda
     */
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

    /**
     * @brief Obtiene nombre de unidad de medida por ID
     *
     * Resuelve el nombre legible de una unidad de medida a partir de su ID,
     * consultando el repositorio correspondiente. Retorna cadena vacía si
     * el ID es null o no se encuentra la entidad.
     *
     * @param unitOfMeasureId ID de la unidad de medida a resolver
     * @param entId ID de la empresa para filtrar unidades de medida
     * @return Nombre de la unidad de medida o cadena vacía si no existe
     */
    private String getUnitOfMeasureName(Long unitOfMeasureId, String entId) {
        if (unitOfMeasureId == null) return "";
        return unitOfMeasurePersistencePort.findByIdAndEnterpriseId(unitOfMeasureId, entId)
                .map(UnitOfMeasure::getName)
                .orElse("");
    }

    /**
     * @brief Obtiene nombre de categoría por ID
     *
     * Resuelve el nombre legible de una categoría a partir de su ID,
     * consultando el repositorio correspondiente. Retorna cadena vacía si
     * el ID es null o no se encuentra la entidad.
     *
     * @param categoryId ID de la categoría a resolver
     * @param entId ID de la empresa para filtrar categorías
     * @return Nombre de la categoría o cadena vacía si no existe
     */
    private String getCategoryName(Long categoryId, String entId) {
        if (categoryId == null) return "";
        return categoryPersistencePort.findByIdAndEnterpriseId(categoryId, entId)
                .map(Category::getName)
                .orElse("");
    }

    /**
     * @brief Obtiene nombre de tipo de producto por ID
     *
     * Resuelve el nombre legible de un tipo de producto a partir de su ID,
     * consultando el repositorio correspondiente. Retorna cadena vacía si
     * el ID es null o no se encuentra la entidad.
     *
     * @param productTypeId ID del tipo de producto a resolver
     * @param entId ID de la empresa para filtrar tipos de producto
     * @return Nombre del tipo de producto o cadena vacía si no existe
     */
    private String getProductTypeName(Long productTypeId, String entId) {
        if (productTypeId == null) return "";
        return productTypePersistencePort.findByIdAndEnterpriseId(productTypeId, entId)
                .map(ProductType::getName)
                .orElse("");
    }

    /**
     * @brief Ajusta automáticamente el ancho de columnas con límites razonables
     *
     * Aplica auto-sizing automático a las 11 columnas del Excel, luego establece
     * límites mínimo (1500 unidades) y máximo (25000 unidades) para evitar
     * anchos extremos. Agrega padding adicional para mejor legibilidad.
     *
     * @param sheet Hoja de trabajo Excel cuyas columnas ajustar
     */
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

    /**
     * @brief Genera archivo Excel de plantilla con filas de ejemplo y validaciones aplicadas
     *
     * Crea un archivo Excel completo con estructura de plantilla: encabezados con estilos,
     * 10 filas de ejemplo para guiar al usuario, validaciones de datos aplicadas
     * y ajuste automático de columnas para una experiencia óptima de uso.
     *
     * @param entId ID de la empresa para obtener datos de validación de entidades relacionadas
     * @return Arreglo de bytes con el archivo Excel de plantilla completo
     * @throws IOException si ocurre error al escribir el archivo Excel
     */
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

    /**
     * @brief Crea estilo para celdas de plantilla con bordes delgados
     *
     * Configura un estilo básico para celdas de plantilla con bordes delgados
     * y alineación vertical centrada, adecuado para filas de ejemplo y celdas
     * vacías que el usuario debe completar.
     *
     * @param workbook Libro de trabajo Excel donde crear el estilo
     * @return Estilo configurado para celdas de plantilla
     */
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

    /**
     * @brief Crea filas de ejemplo en la plantilla con datos de muestra
     *
     * Genera filas de ejemplo en la plantilla Excel para guiar al usuario:
     * la primera fila contiene datos de ejemplo completos, mientras que las
     * filas adicionales se crean vacías para que el usuario las complete.
     *
     * @param sheet Hoja de trabajo Excel donde crear las filas de ejemplo
     * @param templateStyle Estilo visual para las celdas de plantilla
     * @param numberOfRows Número total de filas de ejemplo a crear
     */
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

    /**
     * @brief Crea fila vacía en plantilla con todas las columnas inicializadas
     *
     * Inicializa una fila completa de plantilla con 11 columnas vacías,
     * aplicando el estilo de plantilla a cada celda para mantener consistencia
     * visual en filas que el usuario debe completar.
     *
     * @param row Fila de Excel a inicializar con celdas vacías
     * @param templateStyle Estilo visual a aplicar a todas las celdas de la fila
     */
    private void createEmptyTemplateRow(Row row, CellStyle templateStyle) {
        for (int i = 0; i < 11; i++) {
            createHeaderCell(row, i, "", templateStyle);
        }
    }



    /**
     * @brief Aplica validaciones de datos Excel a la plantilla usando servicio especializado
     *
     * Delega la aplicación de todas las validaciones de datos (listas desplegables,
     * formatos numéricos, textos obligatorios) al servicio especializado de validaciones,
     * configurando un amplio rango de filas para permitir extensas plantillas.
     *
     * @param sheet Hoja de trabajo Excel donde aplicar las validaciones
     * @param entId ID de la empresa para obtener datos de entidades relacionadas
     */
    private void applyValidationsToTemplate(Sheet sheet, String entId) {
        int startRow = 1; // Después del encabezado
        int endRow = 1000; // Permitir muchas filas para la plantilla

        excelValidationService.applyProductValidations(sheet, entId, startRow, endRow);
    }

    /**
     * @brief Genera archivo Excel con datos reales de productos y validaciones aplicadas
     *
     * Crea un archivo Excel completo con productos existentes: encabezados con estilos,
     * datos poblados con resolución de entidades relacionadas, validaciones aplicadas
     * para edición directa, y ajuste automático de columnas.
     *
     * @param products Lista de productos existentes a incluir en el archivo
     * @param entId ID de la empresa para resolver nombres de entidades relacionadas
     * @return Arreglo de bytes con el archivo Excel generado
     * @throws IOException si ocurre error al escribir el archivo Excel
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
     * @brief Aplica validaciones de datos Excel a hoja con productos existentes
     *
     * Aplica validaciones de datos a una hoja que contiene productos existentes,
     * extendiendo el rango de validación más allá de las filas con datos actuales
     * para permitir agregar nuevos productos manteniendo la integridad.
     *
     * @param sheet Hoja de trabajo Excel con datos de productos existentes
     * @param entId ID de la empresa para obtener datos de entidades relacionadas
     * @param dataRowCount Número de filas que contienen datos actuales
     */
    private void applyValidationsToDataSheet(Sheet sheet, String entId, int dataRowCount) {
        int startRow = 1; // Después del encabezado
        int endRow = Math.max(dataRowCount + 100, 1000); // Datos existentes + filas adicionales

        excelValidationService.applyProductValidations(sheet, entId, startRow, endRow);
    }
}