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
                    if (header != null && !header.isEmpty()) {
                        columnMap.put(header, i);
                        foundHeaders.add(header);
                    }
                }
            }
        }

        // Validar que existan los encabezados requeridos
        for (String requiredHeader : ImportConstants.REQUIRED_HEADERS) {
            boolean found = foundHeaders.contains(requiredHeader);
            if (!found) {
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

            // Parsear campos opcionales con validación de formato
            String quantityValue = getCellValueAsString(row, columnMap.get(ImportConstants.QUANTITY_COLUMN));
            if (quantityValue != null && !quantityValue.trim().isEmpty()) {
                Integer quantity = parseIntegerField(quantityValue, rowNumber, columnMap.get(ImportConstants.QUANTITY_COLUMN), errors);
                builder.quantity(quantity);
            }

            String costValue = getCellValueAsString(row, columnMap.get(ImportConstants.COST_COLUMN));
            if (costValue != null && !costValue.trim().isEmpty()) {
                Double cost = parseDoubleField(costValue, rowNumber, columnMap.get(ImportConstants.COST_COLUMN), errors);
                builder.cost(cost);
            }

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
     * Parsea un campo entero desde String con validación de errores.
     */
    private Integer parseIntegerField(String value, int rowNumber, Integer columnIndex, List<ImportErrorDetail> errors) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            long longValue = Long.parseLong(value.trim());
            if (longValue > ImportConstants.Validations.MAX_QUANTITY) {
                errors.add(createValidationError(rowNumber, "INVALID_NUMBER",
                    "La cantidad excede el valor máximo permitido", ImportConstants.QUANTITY_COLUMN, columnIndex));
                return null;
            }
            if (longValue < 0) {
                errors.add(createValidationError(rowNumber, "INVALID_NUMBER",
                    "La cantidad debe ser un valor positivo", ImportConstants.QUANTITY_COLUMN, columnIndex));
                return null;
            }
            return (int) longValue;
        } catch (NumberFormatException e) {
            errors.add(createValidationError(rowNumber, "INVALID_FORMAT",
                "La cantidad debe contener solo números", ImportConstants.QUANTITY_COLUMN, columnIndex));
            return null;
        }
    }

    /**
     * Parsea un campo double desde String con validación de errores.
     */
    private Double parseDoubleField(String value, int rowNumber, Integer columnIndex, List<ImportErrorDetail> errors) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            double doubleValue = Double.parseDouble(value.trim());
            if (doubleValue > ImportConstants.Validations.MAX_COST) {
                errors.add(createValidationError(rowNumber, "INVALID_NUMBER",
                    "El costo excede el valor máximo permitido", ImportConstants.COST_COLUMN, columnIndex));
                return null;
            }
            if (doubleValue < 0) {
                errors.add(createValidationError(rowNumber, "INVALID_NUMBER",
                    "El costo debe ser un valor positivo", ImportConstants.COST_COLUMN, columnIndex));
                return null;
            }
            return doubleValue;
        } catch (NumberFormatException e) {
            errors.add(createValidationError(rowNumber, "INVALID_FORMAT",
                "El costo debe contener solo números", ImportConstants.COST_COLUMN, columnIndex));
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
     * Crea un error de validación.
     */
    private ImportErrorDetail createValidationError(int rowNumber, String errorCode, String message,
                                                   String columnName, Integer columnNumber) {
        return ImportErrorDetail.builder()
                .rowNumber(rowNumber)
                .columnNumber(columnNumber != null ? columnNumber + 1 : null)
                .columnName(columnName)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorType(ImportErrorType.FORMAT_ERROR)
                .build();
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