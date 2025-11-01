package com.products_management.application.service;

import com.products_management.application.ports.input.IProductImportUseCase;
import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.models.ImportErrorDetail;
import com.products_management.domain.models.ProductExcelData;
import com.products_management.infraestructure.input.rest.data.request.ProductImportRequest;
import com.products_management.infraestructure.input.rest.data.response.ProductImportResponse;

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
    private final ProductDuplicateDetectionService duplicateDetectionService;
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

            // 3. Validación por lotes
            ProductBatchValidationService.BatchValidationResult validationResult =
                    batchValidationService.validateBatch(
                            parsingResult.getProductsData(), entId, parsingResult.getColumnMap());

            allErrors.addAll(validationResult.getErrors());

            if (validationResult.getValidRecords().isEmpty()) {
                return responseBuilder.buildFailedResponse(entId, fileName,
                        parsingResult.getTotalRows(), allErrors);
            }

            // 4. Detección de duplicados
            ProductDuplicateDetectionService.DuplicateDetectionResult duplicateResult =
                    duplicateDetectionService.detectDuplicates(validationResult.getValidRecords(), entId);

            allErrors.addAll(duplicateResult.getErrors());

            // Si no hay registros únicos, manejar según la configuración
            if (duplicateResult.getUniqueRecords().isEmpty()) {
                return handleNoUniqueRecords(entId, fileName, parsingResult, duplicateResult, allErrors);
            }

            // 5. Procesamiento por lotes
            ProductBatchProcessor.BatchProcessingResult processingResult =
                    batchProcessor.processBatch(duplicateResult.getUniqueRecords(), entId);

            allErrors.addAll(processingResult.getErrors());

            // 6. Construir respuesta final
            ProductImportResponse response = responseBuilder.buildSuccessResponse(
                    entId,
                    fileName,
                    parsingResult.getTotalRows(),
                    processingResult.getSuccessCount(),
                    processingResult.getFailureCount(),
                    duplicateResult.getDuplicateCount(),
                    allErrors);

            log.info("Product import completed for enterprise {}. Success: {}, Failed: {}, Duplicates: {}",
                    entId, processingResult.getSuccessCount(), processingResult.getFailureCount(),
                    duplicateResult.getDuplicateCount());

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
     * Maneja el caso cuando no hay registros únicos después de detectar duplicados.
     */
    private ProductImportResponse handleNoUniqueRecords(String entId, String fileName,
                                                       ProductExcelParsingService.ExcelParsingResult parsingResult,
                                                       ProductDuplicateDetectionService.DuplicateDetectionResult duplicateResult,
                                                       List<ImportErrorDetail> allErrors) {

        if (duplicateResult.getDuplicateCount() > 0) {
            // Todos son duplicados
            return responseBuilder.buildSuccessResponse(
                    entId,
                    fileName,
                    parsingResult.getTotalRows(),
                    0, // successCount
                    0, // failureCount
                    duplicateResult.getDuplicateCount(),
                    allErrors);
        }

        // No hay registros válidos
        return responseBuilder.buildFailedResponse(entId, fileName,
                parsingResult.getTotalRows(), allErrors);
    }
}