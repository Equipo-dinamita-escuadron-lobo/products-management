package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

/**
 * Repositorio JPA de solo lectura para Category en el contexto de copia.
 * Usa JPQL nativo para bypass del filtro de tenant de Hibernate y leer entOrigen.
 * REQ-PRODUCTS-04.
 */
public interface CategoryCopySourceRepository extends JpaRepository<CategoryEntity, Long> {

    /**
     * Retorna todas las categorías de entOrigen creadas antes del snapshot.
     * Usa enterpriseId para filtrar porque el tenant activo es entDestino durante la copia.
     */
    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :entOrigen AND (c.createdAt IS NULL OR c.createdAt <= :snapshotCorte)")
    List<CategoryEntity> findByEntOrigenBeforeSnapshot(
            @Param("entOrigen") String entOrigen,
            @Param("snapshotCorte") Instant snapshotCorte);
}
