package com.products_management.application.service.importExport;

import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.ProductExcelData;
import com.products_management.domain.utils.ImportConstants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @brief Servicio para procesamiento por lotes de productos importados.
 * Maneja la inserción en BD en lotes transaccionales.
 */
@Service
@RequiredArgsConstructor
public class ProductBatchProcessor {

    private final IProductPersistencePort productPersistencePort;

    /**
     * @brief Procesa lote de productos Excel dividiendo en sub-lotes para optimización
     * @param productsData lista de datos de productos a procesar
     * @param entId ID de la empresa
     * @return resultado del procesamiento por lotes
     */
    @Transactional
    public BatchProcessingResult processBatch(List<ProductExcelData> productsData, String entId) {
        List<ImportErrorDetail> errors = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        // Dividir en lotes más pequeños para procesamiento
        List<List<ProductExcelData>> batches = partitionList(productsData, ImportConstants.Defaults.BATCH_SIZE);

        for (List<ProductExcelData> batch : batches) {
            BatchProcessingResult batchResult = processSingleBatch(batch);
            successCount += batchResult.getSuccessCount();
            failureCount += batchResult.getFailureCount();
            errors.addAll(batchResult.getErrors());
        }

        return BatchProcessingResult.builder()
                .successCount(successCount)
                .failureCount(failureCount)
                .errors(errors)
                .build();
    }

    /**
     * @brief Procesa un sub-lote individual de productos convirtiéndolos y guardándolos
     * @param batch sub-lote de datos Excel a procesar
     * @return resultado del procesamiento del sub-lote
     */
    private BatchProcessingResult processSingleBatch(List<ProductExcelData> batch) {
        List<ImportErrorDetail> errors = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (ProductExcelData productData : batch) {
            try {
                Product product = convertToProduct(productData);
                Product createdProduct = productPersistencePort.create(product);
                createdProduct.generateCode();
                productPersistencePort.create(createdProduct);

                successCount++;

            } catch (Exception e) {
                failureCount++;
                errors.add(ImportErrorDetail.builder()
                        .rowNumber(productData.getRowNumber())
                        .errorCode(ImportConstants.ErrorCodes.SYSTEM_ERROR)
                        .errorMessage("Error creando producto '" + productData.getName() + "': " + e.getMessage())
                        .errorType(ImportErrorType.SYSTEM_ERROR)
                        .build());

                // Si no se debe continuar en error, detener el procesamiento
                if (!ImportConstants.Defaults.CONTINUE_ON_ERROR) {
                    break;
                }
            }
        }

        return BatchProcessingResult.builder()
                .successCount(successCount)
                .failureCount(failureCount)
                .errors(errors)
                .build();
    }

    /**
     * @brief Convierte datos Excel a entidad Product con validaciones y normalización
     * @param excelData datos del producto desde Excel
     * @return entidad Product lista para persistir
     */
    private Product convertToProduct(ProductExcelData excelData) {
        return Product.builder()
                .name(excelData.getName())
                .description(excelData.getDescription())
                .quantity(excelData.getQuantity())
                .unitOfMeasureId(excelData.getUnitOfMeasureId())
                .categoryId(excelData.getCategoryId())
                .enterpriseId(excelData.getEnterpriseId())
                .cost(excelData.getCost() != null ? excelData.getCost() : 0.0)
                .state(true) // Por defecto activo
                .reference(excelData.getReference())
                .productTypeId(excelData.getProductTypeId())
                .presentation(excelData.getPresentation())
                .build();
    }

    /**
     * @brief Divide una lista grande en sub-listas más pequeñas de tamaño fijo
     * @param <T> tipo de elementos en la lista
     * @param list lista original a dividir
     * @param batchSize tamaño máximo de cada sub-lista
     * @return lista de sub-listas particionadas
     */
    private <T> List<List<T>> partitionList(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            batches.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return batches;
    }

    /**
     * @brief Resultado del procesamiento por lotes.     * 
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BatchProcessingResult {
        private int successCount;
        private int failureCount;
        private List<ImportErrorDetail> errors;
    }
}