package com.products_management.application.service;

import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.ProductExcelData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Servicio para detección de duplicados en importación de productos.
 * Detecta duplicados tanto en el archivo Excel como en el sistema existente.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductDuplicateDetectionService {

    private final IProductPersistencePort productPersistencePort;

    /**
     * Detecta duplicados en una lista de productos.
     *
     * @param productsData lista de datos de productos a verificar
     * @param entId ID de la empresa
     * @return resultado de la detección de duplicados
     */
    public DuplicateDetectionResult detectDuplicates(List<ProductExcelData> productsData, String entId) {
        List<ProductExcelData> uniqueRecords = new ArrayList<>();
        List<ImportErrorDetail> errors = new ArrayList<>();
        Set<String> processedReferences = new HashSet<>();
        int duplicateCount = 0;

        log.debug("Detecting duplicates for {} products in enterprise {}", productsData.size(), entId);

        // Verificar duplicados en el sistema existente usando existsByReferenceAndEnterpriseId
        for (ProductExcelData productData : productsData) {
            String reference = productData.getReference();

            if (reference != null) {
                // Verificar duplicado en el sistema existente
                if (productPersistencePort.existsByReferenceAndEnterpriseId(reference, entId)) {
                    errors.add(ImportErrorDetail.builder()
                            .rowNumber(productData.getRowNumber())
                            .errorCode("DUPLICATE_PRODUCT")
                            .errorMessage("Ya existe un producto con la referencia: " + reference)
                            .errorType(ImportErrorType.BUSINESS_RULE_VIOLATION)
                            .fieldValue(reference)
                            .build());
                    duplicateCount++;
                    continue;
                }

                // Verificar duplicado dentro del mismo archivo
                if (processedReferences.contains(reference)) {
                    errors.add(ImportErrorDetail.builder()
                            .rowNumber(productData.getRowNumber())
                            .errorCode("DUPLICATE_IN_FILE")
                            .errorMessage("Referencia duplicada en el archivo: " + reference)
                            .errorType(ImportErrorType.BUSINESS_RULE_VIOLATION)
                            .fieldValue(reference)
                            .build());
                    duplicateCount++;
                    continue;
                }

                processedReferences.add(reference);
            }

            uniqueRecords.add(productData);
        }

        return DuplicateDetectionResult.builder()
                .uniqueRecords(uniqueRecords)
                .errors(errors)
                .duplicateCount(duplicateCount)
                .build();
    }

    /**
     * Resultado de la detección de duplicados.
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DuplicateDetectionResult {
        private List<ProductExcelData> uniqueRecords;
        private List<ImportErrorDetail> errors;
        private int duplicateCount;
    }
}