package com.products_management.application.service;

import com.products_management.application.ports.input.IProductImportUseCase;
import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.ProductExcelData;
import com.products_management.infraestructure.input.rest.dto.request.ProductImportRequest;
import com.products_management.infraestructure.input.rest.dto.response.ProductImportResponse;
import com.products_management.infraestructure.input.validation.ExcelFileValidator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Servicio principal para la importación de productos desde archivos Excel.
 * Orquesta todos los servicios auxiliares para completar el proceso de importación.
 */
@Service
@RequiredArgsConstructor
public class ProductImportService implements IProductImportUseCase {

    private final ExcelFileValidator excelFileValidator;
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
            // 1. Validación de archivo
            excelFileValidator.validate(request.getExcelFile());

            // 2. Parseo de Excel
            ProductExcelParsingService.ExcelParsingResult parsingResult =
                    excelParsingService.parseExcelFile(request.getExcelFile(), entId);

            allErrors.addAll(parsingResult.getErrors());

            if (parsingResult.getProductsData().isEmpty()) {
                return responseBuilder.buildEmptyFileResponse(entId, fileName);
            }

            // Filtrar productos que tienen errores de parsing
            List<ProductExcelData> productsWithoutParsingErrors = filterProductsWithoutParsingErrors(
                    parsingResult.getProductsData(), parsingResult.getErrors());

            // Calcular errores de parsing por fila
            int parsingErrorCount = calculateUniqueFailedRecords(parsingResult.getErrors());

            if (productsWithoutParsingErrors.isEmpty()) {
                // Todos los registros tienen errores de parsing
                return responseBuilder.buildFailedResponse(entId, fileName,
                        parsingResult.getTotalRows(), allErrors);
            }

            // 3. Validación por lotes (solo productos sin errores de parsing)
            ProductBatchValidationService.BatchValidationResult validationResult =
                    batchValidationService.validateBatch(
                            productsWithoutParsingErrors, entId, parsingResult.getColumnMap());

            allErrors.addAll(validationResult.getErrors());

            if (validationResult.getValidRecords().isEmpty()) {
                // Si no hay registros válidos para procesar
                int validationFailures = calculateUniqueFailedRecords(validationResult.getErrors());
                int totalFailures = parsingErrorCount + validationFailures;

                if (validationResult.getDuplicateCount() > 0) {
                    // Hay duplicados (y posiblemente errores)
                    return responseBuilder.buildSuccessResponse(
                            entId,
                            fileName,
                            parsingResult.getTotalRows(),
                            0, // successCount
                            totalFailures, // failureCount
                            validationResult.getDuplicateCount(), // duplicatesSkipped
                            totalFailures > 0 ? allErrors : null); // Mostrar errores solo si hay fallos
                } else {
                    // Solo hay errores de validación o parsing
                    return responseBuilder.buildFailedResponse(entId, fileName,
                            parsingResult.getTotalRows(), allErrors);
                }
            }

            // 4. Procesamiento por lotes (ya no hay detección de duplicados separada)
            ProductBatchProcessor.BatchProcessingResult processingResult =
                    batchProcessor.processBatch(validationResult.getValidRecords(), entId);

            allErrors.addAll(processingResult.getErrors());

            // 5. Construir respuesta final
            return buildFinalResponse(
                    entId,
                    fileName,
                    parsingResult,
                    validationResult,
                    processingResult,
                    allErrors,
                    parsingErrorCount);

        } catch (Exception e) {

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
     * Filtra los productos que no tienen errores de parsing.
     * Solo los productos sin errores de parsing pasan a la validación.
     */
    private List<ProductExcelData> filterProductsWithoutParsingErrors(
            List<ProductExcelData> allProducts, List<ImportErrorDetail> parsingErrors) {

        if (parsingErrors == null || parsingErrors.isEmpty()) {
            return allProducts;
        }

        // Obtener las filas que tienen errores de parsing
        Set<Integer> errorRows = parsingErrors.stream()
                .map(ImportErrorDetail::getRowNumber)
                .collect(Collectors.toSet());

        // Filtrar productos que NO están en las filas con errores
        return allProducts.stream()
                .filter(product -> !errorRows.contains(product.getRowNumber()))
                .toList();
    }

    /**
     * Construye la respuesta final consolidada calculando correctamente las estadísticas.
     */
    private ProductImportResponse buildFinalResponse(String entId, String fileName,
            ProductExcelParsingService.ExcelParsingResult parsingResult,
            ProductBatchValidationService.BatchValidationResult validationResult,
            ProductBatchProcessor.BatchProcessingResult processingResult,
            List<ImportErrorDetail> allErrors,
            int parsingErrorCount) {

        // Calcular fallos de validación: registros únicos con errores
        int validationFailures = calculateUniqueFailedRecords(validationResult.getErrors());

        // Calcular fallos de procesamiento
        int processingFailures = processingResult.getFailureCount();

        // Total de fallos (parsing + validación + procesamiento)
        int totalFailures = parsingErrorCount + validationFailures + processingFailures;

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