package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;
import java.util.List;

public interface IUnitOfMeasureRestMapper {
    UnitOfMeasure toUnitOfMeasure(UnitOfMeasureCreateRequest unitOfMeasureCreateRequest);
    UnitOfMeasureResponse toUnitOfMeasureResponse(UnitOfMeasure unitOfMeasure);
    List<UnitOfMeasureResponse> toUnitOfMeasureResponseList(List<UnitOfMeasure> unitOfMeasureList);
}
