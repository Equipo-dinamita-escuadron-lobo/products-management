package com.products_management.application.service;

import com.products_management.domain.enums.ImportStatus;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.infraestructure.input.rest.dto.response.ProductImportResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @brief Constructor de respuestas para importación de productos
 *
 * Construye respuestas estructuradas con estadísticas de importación y manejo de errores.
 */
@Slf4j
@Service
public class ProductImportResponseBuilder {

    /**
     * @brief Construye respuesta de importación exitosa con estadísticas
     *
     * Crea respuesta completa con métricas de éxito, fallos y estadísticas consolidadas.
     *
     * @param entId ID de la empresa
     * @param fileName Nombre del archivo procesado
     * @param totalRecords Total de registros procesados
     * @param successCount Registros importados exitosamente
     * @param failureCount Registros con errores
     * @param duplicatesSkipped Registros duplicados omitidos
     * @param errors Lista de errores detallados
     * @return Respuesta estructurada de importación
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
     * @brief Construye respuesta para archivo vacío
     *
     * Genera respuesta cuando no se encontraron registros válidos en el archivo.
     *
     * @param entId ID de la empresa
     * @param fileName Nombre del archivo vacío
     * @return Respuesta indicando archivo vacío
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
     * @brief Construye respuesta de importación fallida
     *
     * Crea respuesta para casos donde la importación completa falló con errores.
     *
     * @param entId ID de la empresa
     * @param fileName Nombre del archivo procesado
     * @param totalRecords Total de registros que fallaron
     * @param errors Lista de errores que causaron el fallo
     * @return Respuesta indicando fallo completo
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
     * @brief Determina estado de importación basado en resultados
     *
     * Evalúa métricas de importación para clasificar como completada, con errores o fallida.
     *
     * @param successCount Registros exitosos
     * @param failureCount Registros fallidos
     * @param duplicatesSkipped Duplicados omitidos
     * @return Estado correspondiente de la importación
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