package com.products_management.application.service;

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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio para procesamiento por lotes de productos importados.
 * Maneja la inserción en BD en lotes transaccionales.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductBatchProcessor {

    private final IProductPersistencePort productPersistencePort;

    /**
     * Procesa un lote de productos válidos.
     *
     * @param productsData lista de datos de productos a procesar
     * @param entId ID de la empresa
     * @return resultado del procesamiento por lotes
     */
    @Transactional
    public BatchProcessingResult processBatch(List<ProductExcelData> productsData, String entId) {
        List<ImportErrorDetail> errors = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        log.debug("Processing batch of {} products for enterprise {}", productsData.size(), entId);

        // Dividir en lotes más pequeños para procesamiento
        List<List<ProductExcelData>> batches = partitionList(productsData, ImportConstants.Defaults.BATCH_SIZE);

        for (List<ProductExcelData> batch : batches) {
            BatchProcessingResult batchResult = processSingleBatch(batch, entId);
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
     * Procesa un lote individual de productos.
     */
    private BatchProcessingResult processSingleBatch(List<ProductExcelData> batch, String entId) {
        List<ImportErrorDetail> errors = new ArrayList<>();
        int successCount = 0;
        int failureCount = 0;

        for (ProductExcelData productData : batch) {
            try {
                Product product = convertToProduct(productData);
                productPersistencePort.create(product);
                successCount++;

                log.debug("Successfully created product: {}", product.getName());

            } catch (Exception e) {
                failureCount++;
                errors.add(ImportErrorDetail.builder()
                        .rowNumber(productData.getRowNumber())
                        .errorCode(ImportConstants.ErrorCodes.SYSTEM_ERROR)
                        .errorMessage("Error creando producto '" + productData.getName() + "': " + e.getMessage())
                        .errorType(ImportErrorType.SYSTEM_ERROR)
                        .build());

                log.error("Error creating product at row {}: {}", productData.getRowNumber(), e.getMessage(), e);

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
     * Convierte ProductExcelData a Product.
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
     * Divide una lista en sublistas de tamaño especificado.
     */
    private <T> List<List<T>> partitionList(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            batches.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return batches;
    }

    /**
     * Resultado del procesamiento por lotes.
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