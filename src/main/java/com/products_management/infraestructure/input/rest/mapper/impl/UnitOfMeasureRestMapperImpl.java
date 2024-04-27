package com.products_management.infraestructure.input.rest.mapper.impl;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IUnitOfMeasureRestMapper;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UnitOfMeasureRestMapperImpl implements IUnitOfMeasureRestMapper {
    @Override
    public UnitOfMeasure toUnitOfMeasure(UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        if ( unitOfMeasureCreateRequest == null ) {
            return null;
        }

        UnitOfMeasure.UnitOfMeasureBuilder unitOfMeasure = UnitOfMeasure.builder();

        unitOfMeasure.id( unitOfMeasureCreateRequest.getId() );
        unitOfMeasure.name(unitOfMeasureCreateRequest.getName());
        unitOfMeasure.description( unitOfMeasureCreateRequest.getDescription() );
        unitOfMeasure.abbreviation( unitOfMeasureCreateRequest.getAbbreviation() );

        return unitOfMeasure.build();
    }

    @Override
    public UnitOfMeasureResponse toUnitOfMeasureResponse(UnitOfMeasure unitOfMeasure) {
        if ( unitOfMeasure == null ) {
            return null;
        }

        UnitOfMeasureResponse.UnitOfMeasureResponseBuilder unitOfMeasureResponse = UnitOfMeasureResponse.builder();
        
        unitOfMeasureResponse.id( unitOfMeasure.getId() );
        unitOfMeasureResponse.name( unitOfMeasure.getName() );
        unitOfMeasureResponse.description( unitOfMeasure.getDescription() );
        unitOfMeasureResponse.abbreviation( unitOfMeasure.getAbbreviation() );

        return unitOfMeasureResponse.build();
    }

    @Override
    public List<UnitOfMeasureResponse> toUnitOfMeasureResponseList(List<UnitOfMeasure> unitOfMeasureList) {
        if ( unitOfMeasureList == null ) {
            return null;
        }

        List<UnitOfMeasureResponse> list = new ArrayList<UnitOfMeasureResponse>( unitOfMeasureList.size() );
        for ( UnitOfMeasure unitOfMeasure : unitOfMeasureList ) {
            list.add( toUnitOfMeasureResponse( unitOfMeasure ) );
        }

        return list;
    }
}
