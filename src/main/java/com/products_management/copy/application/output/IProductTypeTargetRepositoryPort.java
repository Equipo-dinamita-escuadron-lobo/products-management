package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;

/**
 * Puerto de salida para insertar ProductType en entDestino.
 * REQ-PRODUCTS-04.
 */
public interface IProductTypeTargetRepositoryPort {

    /**
     * Guarda un nuevo tipo de producto en entDestino.
     *
     * @param entity entidad a guardar (id debe ser null)
     * @return entidad guardada con nuevo ID
     */
    ProductTypeEntity guardar(ProductTypeEntity entity);
}
