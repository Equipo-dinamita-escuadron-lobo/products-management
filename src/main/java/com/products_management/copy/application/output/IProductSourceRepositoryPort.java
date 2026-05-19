package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.ProductEntity;

import java.time.Instant;
import java.util.List;

/**
 * Puerto de salida para leer Product de entOrigen.
 * REQ-PRODUCTS-04.
 */
public interface IProductSourceRepositoryPort {

    /**
     * Retorna todos los productos de entOrigen creados antes del snapshot.
     */
    List<ProductEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte);
}
