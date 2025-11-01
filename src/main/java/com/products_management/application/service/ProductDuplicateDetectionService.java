package com.products_management.application.service;

import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.ProductExcelData;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Servicio para detección de duplicados en importación de productos.
 * Detecta duplicados tanto en el archivo Excel como en el sistema existente.
 */
@Service
@RequiredArgsConstructor
public class ProductDuplicateDetectionService {

    private final IProductPersistencePort productPersistencePort;

    /**
     * Detecta duplicados en una lista de productos.
     * Los duplicados se cuentan pero NO generan errores, solo se omiten.
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


        // Verificar duplicados en el sistema existente usando existsByReferenceAndEnterpriseId
        for (ProductExcelData productData : productsData) {
            String reference = productData.getReference();

            if (reference != null) {
                // Verificar duplicado en el sistema existente
                if (productPersistencePort.existsByReferenceAndEnterpriseId(reference, entId)) {
                    duplicateCount++;
                    continue;
                }

                // Verificar duplicado dentro del mismo archivo
                if (processedReferences.contains(reference)) {
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