package com.products_management.application.service;

import com.products_management.application.ports.output.*;
import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.model.Category;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.ProductExcelData;
import com.products_management.domain.model.ProductType;
import com.products_management.domain.model.UnitOfMeasure;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.LongConsumer;

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
    private final IProductPersistencePort productPersistencePort;

    private static final String COLUMN_NAME = "Nombre";
    private static final String COLUMN_DESCRIPTION = "Descripción";
    private static final String COLUMN_REFERENCE = "Referencia";
    private static final String COLUMN_PRESENTATION = "Presentación";
    private static final String COLUMN_QUANTITY = "Cantidad";
    private static final String COLUMN_COST = "Costo";
    private static final String COLUMN_UNIT_MEASURE = "Unidad de Medida";
    private static final String COLUMN_CATEGORY = "Categoría";
    private static final String COLUMN_PRODUCT_TYPE = "Tipo de Producto";

    private static final String REQUIRED_FIELD_MISSING = "REQUIRED_FIELD_MISSING";
    private static final String ENTITY_NOT_ACTIVE_SUFFIX = " no existe o está inactiva.";
    private static final String ENTITY_NOT_ACTIVE_SUFFIX_MASC = " no existe o está inactivo.";

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
        int duplicateCount = 0;

        for (ProductExcelData productData : productsData) {
            List<ImportErrorDetail> productErrors = validateProduct(productData, columnMap);

            ProductExcelData resolvedData = resolveEntityIds(productData, entId, productErrors, columnMap);

            if (productErrors.isEmpty()) {
                // Verificar si es duplicado por referencia
                String reference = productData.getReference();
                if (reference != null && productPersistencePort.existsByReferenceAndEnterpriseId(reference, entId)) {
                    duplicateCount++;
                } else {
                    // No es duplicado, agregar a registros válidos
                    if (resolvedData != null) {
                        validRecords.add(resolvedData);
                    }
                }
            } else {
                errors.addAll(productErrors);
            }
        }

        return BatchValidationResult.builder()
                .validRecords(validRecords)
                .errors(errors)
                .duplicateCount(duplicateCount)
                .validCount(validRecords.size())
                .build();
    }

    /**
     * Valida un producto individual.
     */
    private List<ImportErrorDetail> validateProduct(ProductExcelData productData,
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
                    REQUIRED_FIELD_MISSING, "El nombre es requerido",
                    COLUMN_NAME, columnMap.get(COLUMN_NAME), productData.getName()));
        }

        if (isNullOrEmpty(productData.getDescription())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    REQUIRED_FIELD_MISSING, "La descripción es requerida",
                    COLUMN_DESCRIPTION, columnMap.get(COLUMN_DESCRIPTION), productData.getDescription()));
        }

        if (isNullOrEmpty(productData.getReference())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    REQUIRED_FIELD_MISSING, "La referencia es requerida",
                    COLUMN_REFERENCE, columnMap.get(COLUMN_REFERENCE), productData.getReference()));
        }

        if (isNullOrEmpty(productData.getPresentation())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    REQUIRED_FIELD_MISSING, "La presentación es requerida",
                    COLUMN_PRESENTATION, columnMap.get(COLUMN_PRESENTATION), productData.getPresentation()));
        }

        if (isNullOrEmpty(productData.getUnitOfMeasureName())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    REQUIRED_FIELD_MISSING, "La Unidad de Medida es requerida",
                    COLUMN_UNIT_MEASURE, columnMap.get(COLUMN_UNIT_MEASURE), productData.getUnitOfMeasureName()));
        }

        if (isNullOrEmpty(productData.getCategoryName())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    REQUIRED_FIELD_MISSING, "La Categoría es requerida",
                    COLUMN_CATEGORY, columnMap.get(COLUMN_CATEGORY), productData.getCategoryName()));
        }

        if (isNullOrEmpty(productData.getProductTypeName())) {
            errors.add(createValidationError(productData.getRowNumber(),
                    REQUIRED_FIELD_MISSING, "El Tipo de Producto es requerido",
                    COLUMN_PRODUCT_TYPE, columnMap.get(COLUMN_PRODUCT_TYPE), productData.getProductTypeName()));
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
                    COLUMN_REFERENCE, columnMap.get(COLUMN_REFERENCE), productData.getReference()));
        }

        // Validar longitud de presentación
        if (productData.getPresentation() != null &&
            productData.getPresentation().length() > 255) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "INVALID_PRESENTATION", "La presentación excede la longitud máxima de 255 caracteres",
                    COLUMN_PRESENTATION, columnMap.get(COLUMN_PRESENTATION), productData.getPresentation()));
        }
    }

    /**
     * Valida campos numéricos.
     */
    private void validateNumericFields(ProductExcelData productData, List<ImportErrorDetail> errors,
                                      Map<String, Integer> columnMap) {
        // Validar cantidad (opcional, pero si tiene valor debe ser positivo)
        if (productData.getQuantity() != null && productData.getQuantity() < 0) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "INVALID_NUMBER", "La cantidad debe ser un valor numerico y positivo",
                    COLUMN_QUANTITY, columnMap.get(COLUMN_QUANTITY), productData.getQuantity().toString()));
        }

        // Validar costo (opcional, pero si tiene valor debe ser positivo)
        if (productData.getCost() != null && productData.getCost() < 0) {
            errors.add(createValidationError(productData.getRowNumber(),
                    "INVALID_NUMBER", "El costo debe ser un valor numerico y positivo",
                    COLUMN_COST, columnMap.get(COLUMN_COST), productData.getCost().toString()));
        }
    }

    /**
     * Resuelve los IDs de entidades relacionadas (categoría, unidad de medida, tipo de producto).
     */
    private ProductExcelData resolveEntityIds(ProductExcelData productData, String entId,
                                             List<ImportErrorDetail> errors, Map<String, Integer> columnMap) {
        try {
            ProductExcelData.ProductExcelDataBuilder builder = productData.toBuilder();

            // Resolver ID de unidad de medida
            if (!resolveEntityId(productData.getUnitOfMeasureName(), entId, builder::unitOfMeasureId,
                    COLUMN_UNIT_MEASURE, columnMap, errors, productData.getRowNumber())) {
                return null;
            }

            // Resolver ID de categoría
            if (!resolveEntityId(productData.getCategoryName(), entId, builder::categoryId,
                    COLUMN_CATEGORY, columnMap, errors, productData.getRowNumber())) {
                return null;
            }

            // Resolver ID de tipo de producto
            if (!resolveEntityId(productData.getProductTypeName(), entId, builder::productTypeId,
                    COLUMN_PRODUCT_TYPE, columnMap, errors, productData.getRowNumber())) {
                return null;
            }

            return builder.build();

        } catch (Exception e) {
            errors.add(ImportErrorDetail.builder()
                    .rowNumber(productData.getRowNumber())
                    .errorCode("ENTITY_RESOLUTION_ERROR")
                    .errorMessage("Error resolviendo entidades relacionadas: " + e.getMessage())
                    .errorType(ImportErrorType.SYSTEM_ERROR)
                    .fieldValue(null)
                    .build());
            return null;
        }
    }

    /**
     * Método genérico para resolver IDs de entidades.
     */
    private boolean resolveEntityId(String entityName, String entId,
                                   LongConsumer idSetter,
                                   String columnConstant, Map<String, Integer> columnMap,
                                   List<ImportErrorDetail> errors, int rowNumber) {
        if (isNullOrEmpty(entityName)) {
            return true; // No hay entidad que resolver, continuar
        }

        Long entityId = resolveEntityByName(entityName.trim(), entId, columnConstant);
        if (entityId != null) {
            idSetter.accept(entityId);
            return true;
        } else {
            addEntityNotFoundError(rowNumber, columnConstant, columnMap, errors, entityName.trim());
            return false;
        }
    }

    /**
     * Resuelve una entidad por nombre usando el tipo apropiado.
     */
    private Long resolveEntityByName(String name, String entId, String entityType) {
        switch (entityType) {
            case COLUMN_UNIT_MEASURE:
                return resolveUnitOfMeasureId(name, entId);
            case COLUMN_CATEGORY:
                return resolveCategoryId(name, entId);
            case COLUMN_PRODUCT_TYPE:
                return resolveProductTypeId(name, entId);
            default:
                return null;
        }
    }

    /**
     * Agrega un error cuando una entidad no se encuentra.
     */
    private void addEntityNotFoundError(int rowNumber, String columnConstant,
                                       Map<String, Integer> columnMap, List<ImportErrorDetail> errors,
                                       String entityName) {
        String errorCode;
        String errorMessage;

        switch (columnConstant) {
            case COLUMN_UNIT_MEASURE:
                errorCode = "UNIT_OF_MEASURE_NOT_FOUND";
                errorMessage = "La unidad de medida " + entityName + ENTITY_NOT_ACTIVE_SUFFIX;
                break;
            case COLUMN_CATEGORY:
                errorCode = "CATEGORY_NOT_FOUND";
                errorMessage = "La categoría " + entityName + ENTITY_NOT_ACTIVE_SUFFIX;
                break;
            case COLUMN_PRODUCT_TYPE:
                errorCode = "PRODUCT_TYPE_NOT_FOUND";
                errorMessage = "El tipo de producto " + entityName + ENTITY_NOT_ACTIVE_SUFFIX_MASC;
                break;
            default:
                errorCode = "ENTITY_NOT_FOUND";
                errorMessage = "La entidad " + entityName + ENTITY_NOT_ACTIVE_SUFFIX;
        }

        errors.add(ImportErrorDetail.builder()
                .rowNumber(rowNumber)
                .columnNumber(columnMap.get(columnConstant) != null ? columnMap.get(columnConstant) + 1 : null)
                .columnName(columnConstant)
                .errorCode(errorCode)
                .errorMessage(errorMessage)
                .errorType(ImportErrorType.VALIDATION_ERROR)
                .fieldValue(entityName)
                .build());
    }

    /**
     * Crea un error de validación.
     */
    private ImportErrorDetail createValidationError(int rowNumber, String errorCode, String message,
                                                   String columnName, Integer columnNumber, String fieldValue) {
        return ImportErrorDetail.builder()
                .rowNumber(rowNumber)
                .columnNumber(columnNumber != null ? columnNumber + 1 : null)
                .columnName(columnName)
                .errorCode(errorCode)
                .errorMessage(message)
                .errorType(ImportErrorType.VALIDATION_ERROR)
                .fieldValue(fieldValue)
                .build();
    }

    /**
     * Verifica si un string es null o vacío.
     */
    private boolean isNullOrEmpty(String value) {
        return value == null || value.trim().isEmpty();
    }

    /**
     * Resuelve el ID de unidad de medida por nombre.
     */
    private Long resolveUnitOfMeasureId(String name, String entId) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        try {
            // Usar búsqueda exacta con paginación pequeña
            var page = unitOfMeasurePersistencePort.findByEnterpriseIdAndSearch(
                    entId, name.trim(), 0, 10, "name", "asc");

            // Buscar coincidencia exacta (case-insensitive) y verificar que esté activa
            return page.getContent().stream()
                    .filter(unit -> unit.getName().equalsIgnoreCase(name.trim()) && unit.isState())
                    .findFirst()
                    .map(UnitOfMeasure::getId)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Resuelve el ID de categoría por nombre.
     */
    private Long resolveCategoryId(String name, String entId) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        try {
            // Usar búsqueda exacta con paginación pequeña
            var page = categoryPersistencePort.findByEnterpriseIdAndSearch(
                    entId, name.trim(), PageRequest.of(0, 10));

            // Buscar coincidencia exacta (case-insensitive) y verificar que esté activa
            return page.getContent().stream()
                    .filter(category -> category.getName().equalsIgnoreCase(name.trim()) && category.isState())
                    .findFirst()
                    .map(Category::getId)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Resuelve el ID de tipo de producto por nombre.
     */
    private Long resolveProductTypeId(String name, String entId) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        try {
            // Usar búsqueda exacta con paginación pequeña
            var page = productTypePersistencePort.findByEnterpriseIdAndSearch(
                    entId, name.trim(), 0, 10, "name", "asc");

            // Buscar coincidencia exacta (case-insensitive) y verificar que esté activo
            return page.getContent().stream()
                    .filter(productType -> productType.getName().equalsIgnoreCase(name.trim()) && productType.isState())
                    .findFirst()
                    .map(ProductType::getId)
                    .orElse(null);
        } catch (Exception e) {
            return null;
        }
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
        private int duplicateCount;
        private int validCount;
    }
}