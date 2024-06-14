package com.products_management.application.ports.output;

import com.products_management.domain.model.UnitOfMeasure;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los puertos de persistencia para el servicio de unidades de medida.
 * Los puertos de persistencia representan las operaciones de almacenamiento y recuperación
 * de datos relacionadas con las unidades de medida.
 */
public interface IUnitOfMeasurePersistencePort {

    /**
     * Busca una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a buscar.
     * @return un Optional que contiene la unidad de medida encontrada, o un Optional vacío si no se encuentra.
     */
    Optional<UnitOfMeasure> findById(Long id);

    /**
     * Obtiene una lista de todas las unidades de medida.
     *
     * @return una lista de todas las unidades de medida.
     */
    List<UnitOfMeasure> findAll();

    /**
     * Crea una nueva unidad de medida.
     *
     * @param unitOfMeasure la unidad de medida a crear.
     * @return la unidad de medida creada.
     */
    UnitOfMeasure create(UnitOfMeasure unitOfMeasure);

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar.
     */
    void deleteById(Long id);

    /**
     * Elimina todas las unidades de medida.
     */
    void deleteAll();
}
