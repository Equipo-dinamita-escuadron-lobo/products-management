package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;

/**
 * Repositorio JPA de solo lectura para ProductType en el contexto de copia.
 * ProductType no tiene @TenantId — filtra por enterpriseId directamente.
 * REQ-PRODUCTS-04.
 */
public interface ProductTypeCopySourceRepository extends JpaRepository<ProductTypeEntity, Long> {

    /**
     * Retorna todos los tipos de producto de entOrigen creados antes del snapshot.
     */
    @Query("SELECT pt FROM ProductTypeEntity pt WHERE pt.enterpriseId = :entOrigen AND pt.createdAt <= :snapshotCorte")
    List<ProductTypeEntity> findByEntOrigenBeforeSnapshot(
            @Param("entOrigen") String entOrigen,
            @Param("snapshotCorte") Instant snapshotCorte);
}
