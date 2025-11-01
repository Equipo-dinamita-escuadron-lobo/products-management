package com.products_management.application.service;

import com.products_management.application.ports.output.*;
import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.ProductExcelData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Servicio para validación por lotes de datos de productos importados.
 * Valida reglas de negocio, referencias foráneas y formatos de datos.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBatchValidationService {

    private final ICategoryPersistencePort categoryPersistencePort;
    private final IUnitOfMeasurePersistencePort unitOfMeasurePersistencePort;
    private final IProductTypePersistencePort productTypePersistencePort;

    /**
     * Valida un lote de datos de productos.
     *
     * @param productsData lista de datos de productos a validar
     * @param entId ID de la empresa
     * @param columnMap mapa de columnas para información de errores
     * @return resultado de la validación por lotes
     */
    public BatchValidationResult validateBatch(List<ProductExcelData> productsData, String entId,
                                               Map<String, Integer> columnMap) {
        List<ProductExcelData> validRecords = new ArrayList<>();
        List<ImportErrorDetail> errors = new ArrayList<>();

        log.debug("Validating batch of {} products for enterprise {}", productsData.size(), entId);

        for (ProductExcelData productData : productsData) {
            List<ImportErrorDetail> productErrors = validateProduct(productData, entId, columnMap);

            if (productErrors.isEmpty()) {
                // Resolver IDs de entidades relacionadas
                ProductExcelData resolvedData = resolveEntityIds(productData, entId, errors);
                if (resolvedData != null) {
                    validRecords.add(resolvedData);
                }
            } else {
                errors.addAll(productErrors);
            }
        }

        return BatchValidationResult.builder()
                .validRecords(validRecords)
                .errors(errors)
                .build();
    }

    /**
     * Valida un producto individual.
     */
    private List<ImportErrorDetail> validateProduct(ProductExcelData productData, String entId,
                                                    Map<String, Integer> columnMap) {
        List<ImportErrorDetail> errors = new ArrayList<>();

        // Validar campos requeridos
        validateRequiredFields(productData, errors, columnMap);

        // Validar formatos y longitudes
        validateFieldFormats(productData, errors, columnMap);

        // Validar valores numéricos
        validateNumericFields(productData, errors, columnMap);

        return errors;
    }

    /**
     * Valida campos requeridos.
     */
    private void validateRequiredFields(ProductExcelData productData, List<ImportErrorDetail> errors,
                                       Map<String, Integer> columnMap) {
        if (isNullOrEmpty(productData.getName())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "REQUIRED_FIELD_MISSING", "El nombre es requerido",
                    columnMap.get("Nombre")));
        }

        if (isNullOrEmpty(productData.getDescription())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "REQUIRED_FIELD_MISSING", "La descripción es requerida",
                    columnMap.get("Descripción")));
        }

        if (isNullOrEmpty(productData.getReference())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "REQUIRED_FIELD_MISSING", "La referencia es requerida",
                    columnMap.get("Referencia")));
        }

        if (isNullOrEmpty(productData.getPresentation())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "REQUIRED_FIELD_MISSING", "La presentación es requerida",
                    columnMap.get("Presentación")));
        }
    }

    /**
     * Valida formatos y longitudes de campos.
     */
    private void validateFieldFormats(ProductExcelData productData, List<ImportErrorDetail> errors,
                                     Map<String, Integer> columnMap) {
        // Validar longitud de referencia
        if (productData.getReference() != null &&
            productData.getReference().length() > 255) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "INVALID_REFERENCE", "La referencia excede la longitud máxima de 255 caracteres",
                    columnMap.get("Referencia")));
        }

        // Validar longitud de presentación
        if (productData.getPresentation() != null &&
            productData.getPresentation().length() > 255) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "INVALID_PRESENTATION", "La presentación excede la longitud máxima de 255 caracteres",
                    columnMap.get("Presentación")));
        }
    }

    /**
     * Valida campos numéricos.
     */
    private void validateNumericFields(ProductExcelData productData, List<ImportErrorDetail> errors,
                                      Map<String, Integer> columnMap) {
        // Validar cantidad
        if (productData.getQuantity() != null && productData.getQuantity() < 0) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "INVALID_NUMBER", "La cantidad no puede ser negativa",
                    columnMap.get("Cantidad")));
        }

        // Validar costo
        if (productData.getCost() != null && productData.getCost() < 0) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "INVALID_NUMBER", "El costo no puede ser negativo",
                    columnMap.get("Costo")));
        }
    }

    /**
     * Resuelve los IDs de entidades relacionadas (categoría, unidad de medida, tipo de producto).
     */
    private ProductExcelData resolveEntityIds(ProductExcelData productData, String entId,
                                             List<ImportErrorDetail> errors) {
        try {
            ProductExcelData.ProductExcelDataBuilder builder = productData.toBuilder();

            // Aquí iría la lógica para resolver IDs desde los nombres
            // Por ahora, dejamos los campos como están para que se manejen en el procesamiento

            return builder.build();

        } catch (Exception e) {
            errors.add(ImportErrorDetail.builder()
                    .rowNumber(productData.getRowNumber())
                    .errorCode("ENTITY_RESOLUTION_ERROR")
                    .errorMessage("Error resolviendo entidades relacionadas: " + e.getMessage())
                    .errorType(ImportErrorType.SYSTEM_ERROR)
                    .build());
            return null;
        }
    }

    /**
     * Crea un error de validación.
     */
    private ImportErrorDetail createValidationError(int rowNumber, String errorCode, String message,
                                                   Integer columnNumber) {
        return ImportErrorDetail.builder()
                .rowNumber(rowNumber)
                .columnNumber(columnNumber != null ? columnNumber + 1 : null)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorType(ImportErrorType.VALIDATION_ERROR)
                .build();
    }

    /**
     * Verifica si un string es null o vacío.
     */
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Resultado de la validación por lotes.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchValidationResult {
        private List<ProductExcelData> validRecords;
        private List<ImportErrorDetail> errors;
    }
}