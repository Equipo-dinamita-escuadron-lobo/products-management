package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;

import java.time.Instant;
import java.util.List;

/**
 * Puerto de salida para leer Category de entOrigen.
 * REQ-PRODUCTS-04.
 */
public interface ICategorySourceRepositoryPort {

    /**
     * Retorna todas las categorías de entOrigen creadas antes del snapshot.
     */
    List<CategoryEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte);
}
