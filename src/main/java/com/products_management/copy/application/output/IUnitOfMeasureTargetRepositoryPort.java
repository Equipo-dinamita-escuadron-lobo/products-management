package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;

/**
 * Puerto de salida para insertar UnitOfMeasure en entDestino.
 * REQ-PRODUCTS-04.
 */
public interface IUnitOfMeasureTargetRepositoryPort {

    /**
     * Guarda una nueva unidad de medida en entDestino.
     * El ID debe ser null (generado por IDENTITY).
     *
     * @param entity entidad a guardar
     * @return entidad guardada con el nuevo ID asignado
     */
    UnitOfMeasureEntity guardar(UnitOfMeasureEntity entity);
}
