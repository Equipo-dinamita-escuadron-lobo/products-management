package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;

import java.util.List;

public interface IUnitOfMeasurePersistenceMapper {
    UnitOfMeasureEntity toUnitOfMeasureEntity(UnitOfMeasure unitOfMeasure);

    UnitOfMeasure toUnitOfMeasure(UnitOfMeasureEntity unitOfMeasureEntity);

    List<UnitOfMeasure> toUnitOfMeasureList(List<UnitOfMeasureEntity> unitOfMeasureEntityList);
}
