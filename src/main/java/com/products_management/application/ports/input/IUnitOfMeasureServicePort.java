package com.products_management.application.ports.input;

import com.products_management.domain.model.UnitOfMeasure;
import java.util.List;

/**
 * Interfaz que define los puertos de entrada para el servicio de unidades de medida.
 * Los puertos de entrada representan las operaciones que pueden ser realizadas
 * sobre las unidades de medida en la aplicación.
 */
public interface IUnitOfMeasureServicePort {

    /**
     * Busca una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a buscar.
     * @return la unidad de medida encontrada, o null si no se encuentra.
     */
    UnitOfMeasure findById(Long id);

    /**
     * Obtiene una lista de todas las unidades de medida asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las unidades de medida de la empresa.
     */
    List<UnitOfMeasure> findAll(String enterpriseId);

    /**
     * Obtiene una lista de todas las unidades de medida activadas asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las unidades de medida activadas de la empresa.
     */
    List<UnitOfMeasure> findActivated(String enterpriseId);

    /**
     * Crea una nueva unidad de medida.
     *
     * @param unitOfMeasure la unidad de medida a crear.
     * @return la unidad de medida creada.
     */
    UnitOfMeasure create(UnitOfMeasure unitOfMeasure);

    /**
     * Actualiza una unidad de medida existente.
     *
     * @param id el ID de la unidad de medida a actualizar.
     * @param unitOfMeasure los datos de la unidad de medida actualizada.
     * @return la unidad de medida actualizada.
     */
    UnitOfMeasure update(Long id, UnitOfMeasure unitOfMeasure);

    /**
     * Elimina una unidad de medida por su ID.
     *
     * @param id el ID de la unidad de medida a eliminar.
     */
    void deleteById(Long id);

    /**
     * Cambia el estado de una unidad de medida (por ejemplo, activado/desactivado).
     *
     * @param id el ID de la unidad de medida cuyo estado se va a cambiar.
     */
    void changeState(Long id);

    /**
     * Elimina todas las unidades de medida.
     */
    void deleteAll();
}
