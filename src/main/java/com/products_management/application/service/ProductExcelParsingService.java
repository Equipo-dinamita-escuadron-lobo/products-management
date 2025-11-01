package com.products_management.application.service;

import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.ProductExcelData;
import com.products_management.domain.utils.ImportConstants;
import com.products_management.domain.utils.StringNormalizer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

/**
 * Servicio especializado en el parseo de archivos Excel para importación de productos.
 * Maneja la lectura, validación de formato y conversión de datos desde Excel.
 */
@Slf4j
@Service
public class ProductExcelParsingService {

    private static final int HEADER_ROW_INDEX = 0;
    private static final int DATA_START_ROW_INDEX = 1;

    /**
     * Parsea el archivo Excel y extrae los datos de productos.
     *
     * @param file archivo Excel a procesar
     * @param entId identificador de la empresa
     * @return resultado del parseo con datos y errores
     */
    public ExcelParsingResult parseExcelFile(MultipartFile file, String entId) {
        List<ProductExcelData> productsData = new ArrayList<>();
        List<ImportErrorDetail> errors = new ArrayList<>();
        Map<String, Integer> columnMap = new HashMap<>();

        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = workbook.getSheetAt(0);

            if (sheet.getPhysicalNumberOfRows() == 0) {
                throw new IllegalArgumentException(ImportConstants.ErrorMessages.EMPTY_FILE);
            }

            // Detectar mapa de columnas dinámicamente
            columnMap = detectColumnMapping(sheet, errors);
            if (columnMap.isEmpty()) {
                throw new IllegalArgumentException(ImportConstants.ErrorMessages.INVALID_HEADERS);
            }

            // Procesar filas de datos (empezar después de headers)
            for (int rowIndex = DATA_START_ROW_INDEX; rowIndex <= sheet.getLastRowNum(); rowIndex++) {
                Row row = sheet.getRow(rowIndex);
                if (row == null || isEmptyRow(row)) {
                    continue;
                }

                ProductExcelData productData = parseRow(row, rowIndex + 1, entId, columnMap, errors);
                if (productData != null) {
                    productsData.add(productData);
                }
            }

        } catch (IOException e) {
            throw new RuntimeException("Error leyendo archivo Excel: " + e.getMessage(), e);
        }

        return ExcelParsingResult.builder()
                .productsData(productsData)
                .errors(errors)
                .totalRows(productsData.size())
                .columnMap(columnMap)
                .build();
    }

    /**
     * Detecta el mapeo de columnas basado en los encabezados del archivo.
     */
    private Map<String, Integer> detectColumnMapping(Sheet sheet, List<ImportErrorDetail> errors) {
        Row headerRow = sheet.getRow(HEADER_ROW_INDEX);
        if (headerRow == null) {
            errors.add(ImportErrorDetail.builder()
                    .rowNumber(1)
                    .errorCode("MISSING_HEADERS")
                    .errorMessage("El archivo no contiene encabezados")
                    .errorType(ImportErrorType.FORMAT_ERROR)
                    .build());
            return new HashMap<>();
        }

        Map<String, Integer> columnMap = new HashMap<>();
        Set<String> foundHeaders = new HashSet<>();

        // Mapear todas las columnas encontradas
        for (int i = 0; i < headerRow.getLastCellNum(); i++) {
            Cell cell = headerRow.getCell(i);
            if (cell != null && cell.getCellType() == CellType.STRING) {
                String headerValue = cell.getStringCellValue();
                if (headerValue != null) {
                String header = StringNormalizer.normalizeHeaderName(headerValue.trim());
                    log.debug("Header original: '{}', normalizado: '{}'", headerValue.trim(), header);
                    if (header != null && !header.isEmpty()) {
                        columnMap.put(header, i);
                        foundHeaders.add(header);
                        log.debug("Header agregado a foundHeaders: '{}'", header);
                    }
                }
            }
        }

        // Validar que existan los encabezados requeridos
        log.debug("Headers encontrados: {}", foundHeaders);
        log.debug("Headers requeridos: {}", Arrays.toString(ImportConstants.REQUIRED_HEADERS));
        for (String requiredHeader : ImportConstants.REQUIRED_HEADERS) {
            log.debug("Verificando header requerido: '{}' (length: {})", requiredHeader, requiredHeader.length());
            boolean found = foundHeaders.contains(requiredHeader);
            log.debug("Header '{}' encontrado: {}", requiredHeader, found);
            if (!found) {
                log.error("Header requerido '{}' no encontrado en headers encontrados: {}", requiredHeader, foundHeaders);
                // Mostrar comparación detallada
                for (String foundHeader : foundHeaders) {
                    log.debug("Comparando '{}' con '{}' - equals: {}, length: {} vs {}", 
                             requiredHeader, foundHeader, requiredHeader.equals(foundHeader), 
                             requiredHeader.length(), foundHeader.length());
                }
                errors.add(ImportErrorDetail.builder()
                        .rowNumber(1)
                        .columnName(requiredHeader)
                        .errorCode("MISSING_REQUIRED_HEADER")
                        .errorMessage("Falta el encabezado requerido: " + requiredHeader)
                        .errorType(ImportErrorType.FORMAT_ERROR)
                        .build());
            }
        }

        return columnMap;
    }

    /**
     * Parsea una fila individual del Excel.
     */
    private ProductExcelData parseRow(Row row, int rowNumber, String entId,
                                       Map<String, Integer> columnMap, List<ImportErrorDetail> errors) {
        try {
            ProductExcelData.ProductExcelDataBuilder builder = ProductExcelData.builder()
                    .rowNumber(rowNumber)
                    .enterpriseId(entId);

            // Parsear campos básicos
            builder.name(getCellValueAsString(row, columnMap.get(ImportConstants.NAME_COLUMN)));
            builder.description(getCellValueAsString(row, columnMap.get(ImportConstants.DESCRIPTION_COLUMN)));
            builder.reference(getCellValueAsString(row, columnMap.get(ImportConstants.REFERENCE_COLUMN)));
            builder.presentation(getCellValueAsString(row, columnMap.get(ImportConstants.PRESENTATION_COLUMN)));

            // Parsear campos opcionales
            builder.quantity(parseIntegerField(getCellValueAsString(row, columnMap.get(ImportConstants.QUANTITY_COLUMN))));
            builder.cost(parseDoubleField(getCellValueAsString(row, columnMap.get(ImportConstants.COST_COLUMN))));

      
            builder.unitOfMeasureName(getCellValueAsString(row, columnMap.get(ImportConstants.UNIT_MEASURE_COLUMN)));
            builder.categoryName(getCellValueAsString(row, columnMap.get(ImportConstants.CATEGORY_COLUMN)));
            builder.productTypeName(getCellValueAsString(row, columnMap.get(ImportConstants.PRODUCT_TYPE_COLUMN)));

            return builder.build();

        } catch (Exception e) {
            errors.add(ImportErrorDetail.builder()
                    .rowNumber(rowNumber)
                    .errorCode("ROW_PARSING_ERROR")
                    .errorMessage("Error parseando fila: " + e.getMessage())
                    .errorType(ImportErrorType.FORMAT_ERROR)
                    .build());
            return null;
        }
    }

    /**
     * Parsea un campo entero desde String.
     */
    private Integer parseIntegerField(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            long longValue = Long.parseLong(value.trim());
            if (longValue > ImportConstants.Validations.MAX_QUANTITY) {
                return null; // Valor demasiado grande
            }
            return (int) longValue;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * Parsea un campo double desde String.
     */
    private Double parseDoubleField(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            double doubleValue = Double.parseDouble(value.trim());
            if (doubleValue > ImportConstants.Validations.MAX_COST) {
                return null; // Valor demasiado grande
            }
            return doubleValue;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String getCellValueAsString(Row row, Integer columnIndex) {
        if (columnIndex == null) {
            return null;
        }

        Cell cell = row.getCell(columnIndex);
        if (cell == null) {
            return null;
        }

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                // Para códigos numéricos, retornar sin decimales
                return String.valueOf((long) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    private boolean isEmptyRow(Row row) {
        for (int i = 0; i < row.getLastCellNum(); i++) {
            Cell cell = row.getCell(i);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                String value = getCellValueAsString(row, i);
                if (value != null && !value.trim().isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Clase que representa el resultado del parseo de Excel.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExcelParsingResult {
        private List<ProductExcelData> productsData;
        private List<ImportErrorDetail> errors;
        private int totalRows;
        private Map<String, Integer> columnMap;
    }
}