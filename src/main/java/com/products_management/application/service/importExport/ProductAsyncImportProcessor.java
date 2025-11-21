package com.products_management.application.service.importExport;

import com.products_management.domain.enums.ImportErrorType;
import com.products_management.domain.enums.ImportStatus;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.ProductExcelData;
import com.products_management.domain.utils.ImportConstants;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @brief Servicio dedicado exclusivamente al procesamiento asíncrono de importaciones de productos
 *
 * Esta clase está separada de ProductImportService para evitar problemas de self-invocation
 * con @Async. Spring requiere que los métodos @Async se llamen desde otra clase para
 * que el proxy AOP funcione correctamente.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ProductAsyncImportProcessor {

    private final ProductExcelParsingService excelParsingService;
    private final ProductBatchValidationService batchValidationService;
    private final ProductBatchProcessor batchProcessor;
    private final ProductImportJobTracker jobTracker;

    /**
     * @brief Procesa la importación de productos de forma asíncrona con tracking de estado
     *
     * Este método se ejecuta en un thread separado del pool de @Async configurado en AsyncConfig.
     *
     * @param fileBytes contenido del archivo Excel en bytes
     * @param entId identificador de la empresa
     * @param fileName nombre del archivo
     * @param jobId identificador único del job
     */
    @Async
    public void processImportAsync(byte[] fileBytes, String entId, String fileName, String jobId) {
        long startTime = System.currentTimeMillis();

        log.info("JobId {}: Iniciando procesamiento ASÍNCRONO de productos en thread: {}",
                jobId, Thread.currentThread().getName());

        java.util.Map<String, Long> phaseTimes = new java.util.LinkedHashMap<>();

        try {
            jobTracker.updateJobStatus(jobId, ImportStatus.PROCESSING);
            jobTracker.updateProgress(jobId, 10);

            // FASE 1: Parseo del archivo Excel desde bytes
            log.info("JobId {}: Fase 1 - Parsing del archivo Excel de productos", jobId);
            long phase1Start = System.currentTimeMillis();
            ProductExcelParsingService.ExcelParsingResult parsingResult =
                    excelParsingService.parseExcelFileFromBytes(fileBytes, entId);
            long phase1Time = System.currentTimeMillis() - phase1Start;
            phaseTimes.put("1. Parsing Excel", phase1Time);
            log.info("JobId {}: Fase 1 completada en {} ms - {} registros encontrados",
                    jobId, phase1Time, parsingResult.getTotalRows());
            jobTracker.updateProgress(jobId, 25);

            if (parsingResult.getProductsData().isEmpty()) {
                handleAsyncError(jobId, "No se encontraron datos válidos para importar",
                        parsingResult.getErrors(), 0);
                return;
            }

            // Filtrar productos sin errores de parsing
            List<ProductExcelData> productsWithoutParsingErrors = filterProductsWithoutParsingErrors(
                    parsingResult.getProductsData(), parsingResult.getErrors());

            int parsingErrorCount = calculateUniqueFailedRecords(parsingResult.getErrors());

            if (productsWithoutParsingErrors.isEmpty()) {
                handleAsyncError(jobId, "Todos los registros tienen errores de parsing",
                        parsingResult.getErrors(), parsingResult.getTotalRows());
                return;
            }

            // FASE 2: Validación de datos en lote
            log.info("JobId {}: Fase 2 - Validación de {} productos",
                    jobId, productsWithoutParsingErrors.size());
            long phase2Start = System.currentTimeMillis();
            ProductBatchValidationService.BatchValidationResult validationResult =
                    batchValidationService.validateBatch(productsWithoutParsingErrors, entId,
                            parsingResult.getColumnMap());
            long phase2Time = System.currentTimeMillis() - phase2Start;
            phaseTimes.put("2. Validación", phase2Time);
            log.info("JobId {}: Fase 2 completada en {} ms - {} válidos, {} errores, {} duplicados",
                    jobId, phase2Time, validationResult.getValidCount(),
                    validationResult.getErrors().size(), validationResult.getDuplicateCount());
            jobTracker.updateProgress(jobId, 50);

            if (validationResult.getValidRecords().isEmpty()) {
                handleValidationOnlyErrors(jobId, parsingResult, validationResult, parsingErrorCount);
                return;
            }

            // FASE 3: Procesamiento por lotes con seguimiento de progreso
            log.info("JobId {}: Fase 3 - Procesamiento de {} productos únicos",
                    jobId, validationResult.getValidRecords().size());
            long phase3Start = System.currentTimeMillis();
            ProcessingResult processingResult = processInBatches(
                    validationResult.getValidRecords(), entId, jobId);
            long phase3Time = System.currentTimeMillis() - phase3Start;
            phaseTimes.put("3. Procesamiento Lotes", phase3Time);
            log.info("JobId {}: Fase 3 completada en {} ms - {} exitosos, {} fallidos",
                    jobId, phase3Time, processingResult.getSuccessCount(), processingResult.getFailureCount());
            jobTracker.updateProgress(jobId, 90);

            // FASE 4: Consolidación de resultados finales
            log.info("JobId {}: Fase 4 - Consolidación de resultados", jobId);
            long phase4Start = System.currentTimeMillis();
            buildFinalAsyncResponse(jobId, entId, fileName, parsingResult, validationResult,
                    processingResult, parsingErrorCount);
            long phase4Time = System.currentTimeMillis() - phase4Start;
            phaseTimes.put("4. Consolidación", phase4Time);
            log.info("JobId {}: Fase 4 completada en {} ms", jobId, phase4Time);

            jobTracker.updateProgress(jobId, 100);

            long totalTime = System.currentTimeMillis() - startTime;
            printPhaseTimesTable(jobId, phaseTimes, totalTime, parsingResult.getTotalRows());

            log.info("JobId {}: Importación de productos completada exitosamente en {} ms", jobId, totalTime);

        } catch (Exception e) {
            log.error("JobId {}: Error crítico durante la importación asíncrona de productos", jobId, e);
            handleAsyncCriticalError(jobId, e);
        }
    }

    /**
     * @brief Imprime una tabla formateada con los tiempos de cada fase
     */
    private void printPhaseTimesTable(String jobId, java.util.Map<String, Long> phaseTimes,
                                      long totalTime, int totalRecords) {
        StringBuilder table = new StringBuilder("\n");
        table.append("╔════════════════════════════════════════════════════════════════════════╗\n");
        table.append(String.format("║  RESUMEN DE TIEMPOS - JobId: %-40s ║\n", jobId));
        table.append("╠════════════════════════════════════════════════════════════════════════╣\n");
        table.append("║  Fase                          │ Tiempo (ms) │ Tiempo (s) │ Porcentaje ║\n");
        table.append("╠════════════════════════════════════════════════════════════════════════╣\n");

        for (java.util.Map.Entry<String, Long> entry : phaseTimes.entrySet()) {
            String phaseName = entry.getKey();
            long phaseTime = entry.getValue();
            double seconds = phaseTime / 1000.0;
            double percentage = (phaseTime * 100.0) / totalTime;

            table.append(String.format("║  %-30s│ %,11d │ %10.2f │   %6.2f%% ║\n",
                    phaseName, phaseTime, seconds, percentage));
        }

        table.append("╠════════════════════════════════════════════════════════════════════════╣\n");
        table.append(String.format("║  TOTAL                         │ %,11d │ %10.2f │  100.00%% ║\n",
                totalTime, totalTime / 1000.0));
        table.append("╠════════════════════════════════════════════════════════════════════════╣\n");

        double recordsPerSecond = totalRecords > 0 ? (totalRecords * 1000.0) / totalTime : 0;
        double msPerRecord = totalRecords > 0 ? totalTime / (double) totalRecords : 0;

        table.append(String.format("║  Total Registros: %-15d                                   ║\n", totalRecords));
        table.append(String.format("║  Rendimiento: %,.2f registros/seg                                ║\n", recordsPerSecond));
        table.append(String.format("║  Tiempo por registro: %.2f ms                                    ║\n", msPerRecord));
        table.append("╚════════════════════════════════════════════════════════════════════════╝");

        log.info("JobId {}: {}", jobId, table.toString());
    }

    /**
     * @brief Procesa registros válidos en lotes optimizados con tracking de progreso
     */
    private ProcessingResult processInBatches(List<ProductExcelData> validRecords, String entId, String jobId) {
        if (validRecords.isEmpty()) {
            return new ProcessingResult(0, 0, new ArrayList<>());
        }

        List<List<ProductExcelData>> batches = createBatches(validRecords, ImportConstants.Defaults.BATCH_SIZE);
        log.info("JobId {}: Dividido en {} lotes de hasta {} registros",
                jobId, batches.size(), ImportConstants.Defaults.BATCH_SIZE);

        int totalSuccess = 0;
        int totalFailure = 0;
        List<ImportErrorDetail> allErrors = new ArrayList<>();

        int totalBatches = batches.size();
        for (int i = 0; i < totalBatches; i++) {
            List<ProductExcelData> batch = batches.get(i);

            try {
                long batchStart = System.currentTimeMillis();
                ProductBatchProcessor.BatchProcessingResult batchResult =
                        batchProcessor.processBatch(batch, entId);
                long batchTime = System.currentTimeMillis() - batchStart;

                totalSuccess += batchResult.getSuccessCount();
                totalFailure += batchResult.getFailureCount();
                allErrors.addAll(batchResult.getErrors());

                int batchProgress = 50 + (40 * (i + 1) / totalBatches);
                jobTracker.updateProgress(jobId, batchProgress);

                if ((i + 1) % 10 == 0 || batchTime > 2000) {
                    double recordsPerSec = (batch.size() * 1000.0) / batchTime;
                    log.info("JobId {}: Lote {}/{} - {} ms ({} reg/seg) - Exitosos: {}, Fallidos: {}",
                            jobId, i + 1, totalBatches, batchTime, String.format("%.2f", recordsPerSec),
                            batchResult.getSuccessCount(), batchResult.getFailureCount());
                } else {
                    log.debug("JobId {}: Lote {}/{} procesado en {} ms - Exitosos: {}, Fallidos: {}",
                            jobId, i + 1, totalBatches, batchTime, batchResult.getSuccessCount(),
                            batchResult.getFailureCount());
                }

            } catch (Exception e) {
                log.error("JobId {}: Error en lote {}: {}", jobId, i + 1, e.getMessage());
                if (!ImportConstants.Defaults.CONTINUE_ON_ERROR) {
                    throw e;
                }
            }
        }

        return new ProcessingResult(totalSuccess, totalFailure, allErrors);
    }

    /**
     * @brief Filtra productos sin errores de parsing
     */
    private List<ProductExcelData> filterProductsWithoutParsingErrors(
            List<ProductExcelData> allProducts, List<ImportErrorDetail> parsingErrors) {

        if (parsingErrors == null || parsingErrors.isEmpty()) {
            return allProducts;
        }

        Set<Integer> errorRows = parsingErrors.stream()
                .map(ImportErrorDetail::getRowNumber)
                .collect(Collectors.toSet());

        return allProducts.stream()
                .filter(product -> !errorRows.contains(product.getRowNumber()))
                .toList();
    }

    /**
     * @brief Consolida y almacena resultados finales en el job tracker
     */
    private void buildFinalAsyncResponse(String jobId, String entId, String fileName,
                                          ProductExcelParsingService.ExcelParsingResult parsingResult,
                                          ProductBatchValidationService.BatchValidationResult validationResult,
                                          ProcessingResult processingResult,
                                          int parsingErrorCount) {

        List<ImportErrorDetail> allErrors = new ArrayList<>();
        allErrors.addAll(parsingResult.getErrors());
        allErrors.addAll(validationResult.getErrors());
        allErrors.addAll(processingResult.getErrors());

        int validationFailures = calculateUniqueFailedRecords(validationResult.getErrors());
        int processingFailures = processingResult.getFailureCount();
        int totalFailures = parsingErrorCount + validationFailures + processingFailures;

        jobTracker.updateJobMetrics(jobId,
                parsingResult.getTotalRows(),
                processingResult.getSuccessCount(),
                totalFailures,
                validationResult.getDuplicateCount());

        jobTracker.addErrors(jobId, allErrors);

        ImportStatus finalStatus;
        if (totalFailures > 0 && processingResult.getSuccessCount() > 0) {
            finalStatus = ImportStatus.COMPLETED_WITH_ERRORS;
        } else if (processingResult.getSuccessCount() == 0) {
            finalStatus = ImportStatus.FAILED;
        } else {
            finalStatus = ImportStatus.COMPLETED;
        }

        jobTracker.updateJobStatus(jobId, finalStatus);
    }

    /**
     * @brief Maneja el caso cuando solo hay errores de validación sin procesamiento
     */
    private void handleValidationOnlyErrors(String jobId,
                                             ProductExcelParsingService.ExcelParsingResult parsingResult,
                                             ProductBatchValidationService.BatchValidationResult validationResult,
                                             int parsingErrorCount) {

        List<ImportErrorDetail> allErrors = new ArrayList<>();
        allErrors.addAll(parsingResult.getErrors());
        allErrors.addAll(validationResult.getErrors());

        int validationFailures = calculateUniqueFailedRecords(validationResult.getErrors());
        int totalFailures = parsingErrorCount + validationFailures;

        jobTracker.updateJobMetrics(jobId,
                parsingResult.getTotalRows(),
                0,
                totalFailures,
                validationResult.getDuplicateCount());

        jobTracker.addErrors(jobId, allErrors);
        jobTracker.updateJobStatus(jobId, ImportStatus.FAILED);
        jobTracker.updateProgress(jobId, 100);
    }

    /**
     * @brief Maneja errores durante el procesamiento asíncrono
     */
    private void handleAsyncError(String jobId, String errorMessage, List<ImportErrorDetail> errors, int totalRows) {
        log.error("JobId {}: {}", jobId, errorMessage);
        jobTracker.updateJobMetrics(jobId, totalRows, 0, totalRows, 0);
        jobTracker.addErrors(jobId, errors);
        jobTracker.updateJobStatus(jobId, ImportStatus.FAILED);
        jobTracker.updateProgress(jobId, 100);
    }

    /**
     * @brief Maneja errores críticos durante el procesamiento
     */
    private void handleAsyncCriticalError(String jobId, Exception e) {
        List<ImportErrorDetail> systemErrors = List.of(
                ImportErrorDetail.builder()
                        .errorCode(ImportConstants.ErrorCodes.SYSTEM_ERROR)
                        .errorMessage("Error del sistema durante la importación: " + e.getMessage())
                        .errorType(ImportErrorType.SYSTEM_ERROR)
                        .build());

        jobTracker.addErrors(jobId, systemErrors);
        jobTracker.updateJobStatus(jobId, ImportStatus.FAILED);
        jobTracker.updateProgress(jobId, 100);
    }

    /**
     * @brief Divide una lista en sub-listas (batches)
     */
    private <T> List<List<T>> createBatches(List<T> list, int batchSize) {
        List<List<T>> batches = new ArrayList<>();
        for (int i = 0; i < list.size(); i += batchSize) {
            batches.add(list.subList(i, Math.min(i + batchSize, list.size())));
        }
        return batches;
    }

    /**
     * @brief Calcula registros únicos con errores
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
     * @brief Resultado del procesamiento por lotes
     */
    @Getter
    @AllArgsConstructor
    private static class ProcessingResult {
        private final int successCount;
        private final int failureCount;
        private final List<ImportErrorDetail> errors;
    }
}

