package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.ProductEntity;

/**
 * Puerto de salida para insertar Product en entDestino.
 * REQ-PRODUCTS-04.
 */
public interface IProductTargetRepositoryPort {

    /**
     * Guarda un nuevo producto en entDestino.
     *
     * @param entity entidad a guardar (id debe ser null)
     * @return entidad guardada con nuevo ID
     */
    ProductEntity guardar(ProductEntity entity);
}
