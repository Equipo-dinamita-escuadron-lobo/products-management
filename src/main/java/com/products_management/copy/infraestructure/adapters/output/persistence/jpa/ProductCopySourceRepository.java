package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

/**
 * Repositorio JPA de solo lectura para Product en el contexto de copia.
 * REQ-PRODUCTS-04.
 */
public interface ProductCopySourceRepository extends JpaRepository<ProductEntity, Long> {

    /**
     * Retorna todos los productos de entOrigen creados antes del snapshot.
     * Usa enterpriseId para filtrar (bypass de tenant Hibernate en lectura de origen).
     */
    @Query("SELECT p FROM ProductEntity p WHERE p.enterpriseId = :entOrigen AND (p.createdAt IS NULL OR p.createdAt <= :snapshotCorte)")
    List<ProductEntity> findByEntOrigenBeforeSnapshot(
            @Param("entOrigen") String entOrigen,
            @Param("snapshotCorte") Instant snapshotCorte);
}
