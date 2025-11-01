package com.products_management.application.service;

import com.products_management.application.ports.input.IProductImportUseCase;
import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.infraestructure.input.rest.dto.request.ProductImportRequest;
import com.products_management.infraestructure.input.rest.dto.response.ProductImportResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Servicio principal para la importación de productos desde archivos Excel.
 * Orquesta todos los servicios auxiliares para completar el proceso de importación.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductImportService implements IProductImportUseCase {

    private final ProductFileValidationService fileValidationService;
    private final ProductExcelParsingService excelParsingService;
    private final ProductBatchValidationService batchValidationService;
    private final ProductBatchProcessor batchProcessor;
    private final ProductImportResponseBuilder responseBuilder;

    /**
     * Importa productos desde un archivo Excel.
     */
    @Override
    public ProductImportResponse importProductsFromExcel(ProductImportRequest request) {
        String entId = request.getEntId();
        String fileName = request.getExcelFile().getOriginalFilename();
        List<ImportErrorDetail> allErrors = new ArrayList<>();

        try {
            log.info("Starting product import for enterprise {} with file {}", entId, fileName);

            // 1. Validación de archivo
            fileValidationService.validate(request.getExcelFile());

            // 2. Parseo de Excel
            ProductExcelParsingService.ExcelParsingResult parsingResult =
                    excelParsingService.parseExcelFile(request.getExcelFile(), entId);

            allErrors.addAll(parsingResult.getErrors());

            if (parsingResult.getProductsData().isEmpty()) {
                return responseBuilder.buildEmptyFileResponse(entId, fileName);
            }

            // 3. Validación por lotes (incluye detección de duplicados)
            ProductBatchValidationService.BatchValidationResult validationResult =
                    batchValidationService.validateBatch(
                            parsingResult.getProductsData(), entId, parsingResult.getColumnMap());

            allErrors.addAll(validationResult.getErrors());

            if (validationResult.getValidRecords().isEmpty()) {
                // Si no hay registros válidos para procesar
                int validationFailures = calculateUniqueFailedRecords(validationResult.getErrors());
                int duplicatesFound = validationResult.getDuplicateCount();

                if (duplicatesFound > 0) {
                    // Hay duplicados (y posiblemente errores)
                    return responseBuilder.buildSuccessResponse(
                            entId,
                            fileName,
                            parsingResult.getTotalRows(),
                            0, // successCount
                            validationFailures, // failureCount (errores de validación)
                            duplicatesFound, // duplicatesSkipped
                            validationFailures > 0 ? allErrors : null); // Mostrar errores solo si hay fallos
                } else {
                    // Solo hay errores de validación
                    return responseBuilder.buildFailedResponse(entId, fileName,
                            parsingResult.getTotalRows(), allErrors);
                }
            }

            // 4. Procesamiento por lotes (ya no hay detección de duplicados separada)
            ProductBatchProcessor.BatchProcessingResult processingResult =
                    batchProcessor.processBatch(validationResult.getValidRecords(), entId);

            allErrors.addAll(processingResult.getErrors());

            // 5. Construir respuesta final
            ProductImportResponse response = buildFinalResponse(
                    entId,
                    fileName,
                    parsingResult,
                    validationResult,
                    processingResult,
                    allErrors);

            log.info("Product import completed for enterprise {}. Success: {}, Failed: {}, Duplicates: {}",
                    entId, processingResult.getSuccessCount(), processingResult.getFailureCount(),
                    validationResult.getDuplicateCount());

            return response;

        } catch (Exception e) {
            log.error("Unexpected error during product import for enterprise {}: {}", entId, e.getMessage(), e);

            allErrors.add(ImportErrorDetail.builder()
                    .errorCode("SYSTEM_ERROR")
                    .errorMessage("Error del sistema: " + e.getMessage())
                    .errorType(ImportErrorType.SYSTEM_ERROR)
                    .build());

            return responseBuilder.buildFailedResponse(entId, fileName, 0, allErrors);
        }
    }

    /**
     * Calcula el número de registros únicos que tienen errores de validación.
     * Un registro puede tener múltiples errores, pero solo cuenta como 1 fallo.
     */
    private int calculateUniqueFailedRecords(List<ImportErrorDetail> errors) {
        if (errors == null || errors.isEmpty()) {
            return 0;
        }
        return (int) errors.stream()
                .mapToInt(ImportErrorDetail::getRowNumber)
                .distinct()
                .count();
    }

    /**
     * Construye la respuesta final consolidada calculando correctamente las estadísticas.
     */
    private ProductImportResponse buildFinalResponse(String entId, String fileName,
            ProductExcelParsingService.ExcelParsingResult parsingResult,
            ProductBatchValidationService.BatchValidationResult validationResult,
            ProductBatchProcessor.BatchProcessingResult processingResult,
            List<ImportErrorDetail> allErrors) {

        // Calcular fallos de validación: registros únicos con errores
        int validationFailures = calculateUniqueFailedRecords(validationResult.getErrors());

        // Calcular fallos de procesamiento
        int processingFailures = processingResult.getFailureCount();

        // Total de fallos
        int totalFailures = validationFailures + processingFailures;

        return responseBuilder.buildSuccessResponse(
                entId,
                fileName,
                parsingResult.getTotalRows(),
                processingResult.getSuccessCount(),
                totalFailures,
                validationResult.getDuplicateCount(),
                allErrors);
    }
}