package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

/**
 * Repositorio JPA de solo lectura para UnitOfMeasure en el contexto de copia.
 * Consulta sin filtro de tenant (usa enterpriseId directamente) porque
 * UnitOfMeasure no tiene @TenantId.
 * REQ-PRODUCTS-04.
 */
public interface UnitOfMeasureCopySourceRepository extends JpaRepository<UnitOfMeasureEntity, Long> {

    /**
     * Retorna todas las unidades de medida de entOrigen creadas antes del snapshot.
     * Filtra por enterpriseId porque UnitOfMeasure no tiene @TenantId.
     */
    @Query("SELECT u FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :entOrigen AND (u.createdAt IS NULL OR u.createdAt <= :snapshotCorte)")
    List<UnitOfMeasureEntity> findByEntOrigenBeforeSnapshot(
            @Param("entOrigen") String entOrigen,
            @Param("snapshotCorte") Instant snapshotCorte);
}
