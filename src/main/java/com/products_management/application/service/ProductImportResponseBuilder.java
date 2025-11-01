package com.products_management.application.service;

import com.products_management.domain.enums.ImportStatus;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.infraestructure.input.rest.dto.response.ProductImportResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio para construir respuestas de importación de productos.
 * Centraliza la lógica de construcción de respuestas con estadísticas.
 */
@Slf4j
@Service
public class ProductImportResponseBuilder {

    /**
     * Construye respuesta de importación exitosa.
     */
    public ProductImportResponse buildSuccessResponse(String entId, String fileName, int totalRecords,
                                                     int successCount, int failureCount, int duplicatesSkipped,
                                                     List<ImportErrorDetail> errors) {
        ImportStatus status = determineStatus(successCount, failureCount, duplicatesSkipped);

        return ProductImportResponse.builder()
                .entId(entId)
                .fileName(fileName)
                .status(status)
                .totalRecords(totalRecords)
                .successfulImports(successCount)
                .failedImports(failureCount)
                .duplicatesSkipped(duplicatesSkipped)
                .errors(errors == null || errors.isEmpty() ? null : errors)
                .build();
    }

    /**
     * Construye respuesta cuando el archivo está vacío.
     */
    public ProductImportResponse buildEmptyFileResponse(String entId, String fileName) {
        return ProductImportResponse.builder()
                .entId(entId)
                .fileName(fileName)
                .status(ImportStatus.FAILED)
                .totalRecords(0)
                .successfulImports(0)
                .failedImports(0)
                .duplicatesSkipped(0)
                .build();
    }

    /**
     * Construye respuesta cuando falló la importación.
     */
    public ProductImportResponse buildFailedResponse(String entId, String fileName, int totalRecords,
                                                    List<ImportErrorDetail> errors) {
        return ProductImportResponse.builder()
                .entId(entId)
                .fileName(fileName)
                .status(ImportStatus.FAILED)
                .totalRecords(totalRecords)
                .successfulImports(0)
                .failedImports(totalRecords)
                .duplicatesSkipped(0)
                .errors(errors == null || errors.isEmpty() ? null : errors)
                .build();
    }

    /**
     * Determina el estado de la importación basado en los resultados.
     */
    private ImportStatus determineStatus(int successCount, int failureCount, int duplicatesSkipped) {
        if (successCount > 0) {
            if (failureCount > 0 || duplicatesSkipped > 0) {
                return ImportStatus.COMPLETED_WITH_ERRORS;
            } else {
                return ImportStatus.COMPLETED;
            }
        } else if (failureCount == 0 && duplicatesSkipped > 0) {
            return ImportStatus.COMPLETED;
        } else {
            return ImportStatus.FAILED;
        }
    }
}