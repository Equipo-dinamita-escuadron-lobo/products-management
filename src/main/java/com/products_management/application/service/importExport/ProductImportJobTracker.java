package com.products_management.application.service.importExport;

import com.products_management.domain.enums.ImportStatus;
import com.products_management.domain.model.ImportErrorDetail;
import com.products_management.domain.model.ImportJobStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @brief Servicio de tracking en memoria para jobs de importación asíncrona de productos
 *
 * Mantiene un registro en memoria de todos los jobs de importación activos y completados,
 * permitiendo consultar su estado, progreso y resultados. Utiliza ConcurrentHashMap
 * para garantizar thread-safety en operaciones concurrentes.
 */
@Slf4j
@Service
public class ProductImportJobTracker {

    private final ConcurrentHashMap<String, ImportJobStatus> jobStatusMap = new ConcurrentHashMap<>();

    /**
     * @brief Crea un nuevo job de importación y retorna su ID único
     * @param enterpriseId identificador de la empresa
     * @param fileName nombre del archivo a importar
     * @return ID único del job creado
     */
    public String createJob(String enterpriseId, String fileName) {
        String jobId = UUID.randomUUID().toString();

        ImportJobStatus jobStatus = ImportJobStatus.builder()
                .jobId(jobId)
                .enterpriseId(enterpriseId)
                .fileName(fileName)
                .status(ImportStatus.PENDING)
                .startTime(LocalDateTime.now())
                .progress(0)
                .totalRecords(0)
                .successfulImports(0)
                .failedImports(0)
                .duplicatesSkipped(0)
                .build();

        jobStatusMap.put(jobId, jobStatus);

        log.info("Job de importación de productos creado: {} para empresa: {} archivo: {}", 
                jobId, enterpriseId, fileName);

        return jobId;
    }

    /**
     * @brief Actualiza el estado de un job de importación
     * @param jobId identificador del job
     * @param status nuevo estado
     */
    public void updateJobStatus(String jobId, ImportStatus status) {
        ImportJobStatus jobStatus = jobStatusMap.get(jobId);
        if (jobStatus != null) {
            jobStatus.setStatus(status);

            if (status.isFinished()) {
                jobStatus.setEndTime(LocalDateTime.now());
                jobStatus.setProgress(100);
                log.info("Job de importación de productos finalizado: {} con estado: {}", jobId, status);
            }
        } else {
            log.warn("Intento de actualizar job de productos inexistente: {}", jobId);
        }
    }

    /**
     * @brief Actualiza las métricas de un job de importación
     * @param jobId identificador del job
     * @param totalRecords total de registros
     * @param successfulImports registros exitosos
     * @param failedImports registros fallidos
     * @param duplicatesSkipped duplicados omitidos
     */
    public void updateJobMetrics(String jobId, Integer totalRecords, Integer successfulImports,
                                  Integer failedImports, Integer duplicatesSkipped) {
        ImportJobStatus jobStatus = jobStatusMap.get(jobId);
        if (jobStatus != null) {
            jobStatus.updateMetrics(totalRecords, successfulImports, failedImports, duplicatesSkipped);
            log.debug("Métricas actualizadas para job de productos: {} - Total: {}, Exitosos: {}, Fallidos: {}, Duplicados: {}",
                    jobId, totalRecords, successfulImports, failedImports, duplicatesSkipped);
        }
    }

    /**
     * @brief Actualiza el progreso de un job de importación
     * @param jobId identificador del job
     * @param progress porcentaje de progreso (0-100)
     */
    public void updateProgress(String jobId, Integer progress) {
        ImportJobStatus jobStatus = jobStatusMap.get(jobId);
        if (jobStatus != null) {
            jobStatus.setProgress(Math.min(100, Math.max(0, progress)));
            log.debug("Progreso actualizado para job de productos: {} - {}%", jobId, progress);
        }
    }

    /**
     * @brief Agrega errores al job de importación
     * @param jobId identificador del job
     * @param errors lista de errores a agregar
     */
    public void addErrors(String jobId, List<ImportErrorDetail> errors) {
        ImportJobStatus jobStatus = jobStatusMap.get(jobId);
        if (jobStatus != null) {
            jobStatus.addErrors(errors);
        }
    }

    /**
     * @brief Obtiene el estado actual de un job de importación
     * @param jobId identificador del job
     * @return Optional con el estado del job si existe
     */
    public Optional<ImportJobStatus> getJobStatus(String jobId) {
        return Optional.ofNullable(jobStatusMap.get(jobId));
    }

    /**
     * @brief Elimina un job del tracker (útil para limpieza de memoria)
     * @param jobId identificador del job
     */
    public void removeJob(String jobId) {
        ImportJobStatus removed = jobStatusMap.remove(jobId);
        if (removed != null) {
            log.info("Job de productos removido del tracker: {}", jobId);
        }
    }

    /**
     * @brief Obtiene la cantidad de jobs actualmente trackeados
     * @return cantidad de jobs en memoria
     */
    public int getActiveJobsCount() {
        return jobStatusMap.size();
    }
}

