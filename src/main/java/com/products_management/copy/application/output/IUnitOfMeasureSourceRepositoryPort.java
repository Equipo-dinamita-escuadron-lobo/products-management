package com.products_management.copy.application.output;

import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;

import java.time.Instant;
import java.util.List;

/**
 * Puerto de salida para leer UnitOfMeasure de entOrigen.
 * Usa la entidad JPA existente para no duplicar el modelo.
 * REQ-PRODUCTS-04.
 */
public interface IUnitOfMeasureSourceRepositoryPort {

    /**
     * Retorna todas las unidades de medida de entOrigen creadas antes del snapshot.
     *
     * @param entOrigen      identificador de la empresa origen
     * @param snapshotCorte  corte temporal
     * @return lista de entidades fuente
     */
    List<UnitOfMeasureEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte);
}
