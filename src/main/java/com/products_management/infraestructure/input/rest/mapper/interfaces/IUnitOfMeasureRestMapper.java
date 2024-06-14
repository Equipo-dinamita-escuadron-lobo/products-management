package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;

import java.util.List;

/**
 * Interfaz que define métodos para mapear entre entidades de unidad de medida y sus representaciones REST.
 */
public interface IUnitOfMeasureRestMapper {

    /**
     * Convierte una solicitud de creación de unidad de medida ({@link UnitOfMeasureCreateRequest}) en una entidad de unidad de medida ({@link UnitOfMeasure}).
     *
     * @param unitOfMeasureCreateRequest la solicitud de creación de unidad de medida.
     * @return la entidad de unidad de medida convertida.
     */
    UnitOfMeasure toUnitOfMeasure(UnitOfMeasureCreateRequest unitOfMeasureCreateRequest);

    /**
     * Convierte una entidad de unidad de medida ({@link UnitOfMeasure}) en una respuesta de unidad de medida ({@link UnitOfMeasureResponse}).
     *
     * @param unitOfMeasure la entidad de unidad de medida.
     * @return la respuesta de unidad de medida convertida.
     */
    UnitOfMeasureResponse toUnitOfMeasureResponse(UnitOfMeasure unitOfMeasure);

    /**
     * Convierte una lista de entidades de unidad de medida ({@link UnitOfMeasure}) en una lista de respuestas de unidad de medida ({@link UnitOfMeasureResponse}).
     *
     * @param unitOfMeasureList la lista de entidades de unidad de medida.
     * @return la lista de respuestas de unidad de medida convertida.
     */
    List<UnitOfMeasureResponse> toUnitOfMeasureResponseList(List<UnitOfMeasure> unitOfMeasureList);
}
