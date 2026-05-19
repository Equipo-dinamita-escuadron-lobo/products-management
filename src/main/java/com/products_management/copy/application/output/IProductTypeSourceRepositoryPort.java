package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;

import java.time.Instant;
import java.util.List;

/**
 * Puerto de salida para leer ProductType de entOrigen.
 * REQ-PRODUCTS-04.
 */
public interface IProductTypeSourceRepositoryPort {

    /**
     * Retorna todos los tipos de producto de entOrigen creados antes del snapshot.
     */
    List<ProductTypeEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte);
}
