package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;

/**
 * Puerto de salida para insertar Category en entDestino.
 * REQ-PRODUCTS-04.
 */
public interface ICategoryTargetRepositoryPort {

    /**
     * Guarda una nueva categoría en entDestino.
     * El tenant override debe aplicarse antes de llamar a este puerto.
     *
     * @param entity entidad a guardar (id debe ser null)
     * @return entidad guardada con nuevo ID
     */
    CategoryEntity guardar(CategoryEntity entity);
}
