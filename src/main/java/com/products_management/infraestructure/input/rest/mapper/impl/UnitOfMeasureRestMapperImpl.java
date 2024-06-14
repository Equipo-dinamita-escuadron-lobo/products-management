package com.products_management.infraestructure.input.rest.mapper.impl;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IUnitOfMeasureRestMapper;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link IUnitOfMeasureRestMapper} que realiza la conversión entre entidades de unidad de medida y sus representaciones REST.
 */
@Component
public class UnitOfMeasureRestMapperImpl implements IUnitOfMeasureRestMapper {

    /**
     * Convierte una solicitud de creación de unidad de medida ({@link UnitOfMeasureCreateRequest}) en una entidad de unidad de medida ({@link UnitOfMeasure}).
     *
     * @param unitOfMeasureCreateRequest la solicitud de creación de unidad de medida.
     * @return la entidad de unidad de medida convertida.
     */
    @Override
    public UnitOfMeasure toUnitOfMeasure(UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        if (unitOfMeasureCreateRequest == null) {
            return null;
        }

        UnitOfMeasure.UnitOfMeasureBuilder unitOfMeasureBuilder = UnitOfMeasure.builder();
        unitOfMeasureBuilder.id(unitOfMeasureCreateRequest.getId());
        unitOfMeasureBuilder.name(unitOfMeasureCreateRequest.getName());
        unitOfMeasureBuilder.description(unitOfMeasureCreateRequest.getDescription());
        unitOfMeasureBuilder.abbreviation(unitOfMeasureCreateRequest.getAbbreviation());
        unitOfMeasureBuilder.state(unitOfMeasureCreateRequest.getState());
        unitOfMeasureBuilder.enterpriseId(unitOfMeasureCreateRequest.getEnterpriseId());

        return unitOfMeasureBuilder.build();
    }

    /**
     * Convierte una entidad de unidad de medida ({@link UnitOfMeasure}) en una respuesta de unidad de medida ({@link UnitOfMeasureResponse}).
     *
     * @param unitOfMeasure la entidad de unidad de medida.
     * @return la respuesta de unidad de medida convertida.
     */
    @Override
    public UnitOfMeasureResponse toUnitOfMeasureResponse(UnitOfMeasure unitOfMeasure) {
        if (unitOfMeasure == null) {
            return null;
        }

        UnitOfMeasureResponse.UnitOfMeasureResponseBuilder unitOfMeasureResponseBuilder = UnitOfMeasureResponse.builder();
        unitOfMeasureResponseBuilder.id(unitOfMeasure.getId());
        unitOfMeasureResponseBuilder.name(unitOfMeasure.getName());
        unitOfMeasureResponseBuilder.description(unitOfMeasure.getDescription());
        unitOfMeasureResponseBuilder.abbreviation(unitOfMeasure.getAbbreviation());
        unitOfMeasureResponseBuilder.state(unitOfMeasure.getState());
        unitOfMeasureResponseBuilder.enterpriseId(unitOfMeasure.getEnterpriseId());

        return unitOfMeasureResponseBuilder.build();
    }

    /**
     * Convierte una lista de entidades de unidad de medida ({@link UnitOfMeasure}) en una lista de respuestas de unidad de medida ({@link UnitOfMeasureResponse}).
     *
     * @param unitOfMeasureList la lista de entidades de unidad de medida.
     * @return la lista de respuestas de unidad de medida convertida.
     */
    @Override
    public List<UnitOfMeasureResponse> toUnitOfMeasureResponseList(List<UnitOfMeasure> unitOfMeasureList) {
        if (unitOfMeasureList == null) {
            return null;
        }

        List<UnitOfMeasureResponse> unitOfMeasureResponseList = new ArrayList<>(unitOfMeasureList.size());
        for (UnitOfMeasure unitOfMeasure : unitOfMeasureList) {
            unitOfMeasureResponseList.add(toUnitOfMeasureResponse(unitOfMeasure));
        }

        return unitOfMeasureResponseList;
    }
}
