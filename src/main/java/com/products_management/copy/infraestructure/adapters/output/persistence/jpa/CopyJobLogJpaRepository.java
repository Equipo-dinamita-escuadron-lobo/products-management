package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para el log de idempotencia de trabajos de copia.
 * REQ-PRODUCTS-05.
 */
public interface CopyJobLogJpaRepository extends JpaRepository<CopyJobLogEntity, Long> {

    /**
     * Busca un log por la combinación idProceso + fase para verificar idempotencia.
     */
    Optional<CopyJobLogEntity> findByIdProcesoAndFase(String idProceso, Integer fase);

    /**
     * Busca el log más reciente de un proceso (para status y cancel).
     */
    Optional<CopyJobLogEntity> findTopByIdProcesoOrderByFechaInicioDesc(String idProceso);

    /**
     * Elimina todos los logs de un proceso (para cleanup).
     */
    void deleteByIdProceso(String idProceso);
}
