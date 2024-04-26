package com.products_management.infraestructure.output.persistence.mapper.impl;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IUnitOfMeasurePersistenceMapper;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class UnitOfMeasurePersistenceMapperImpl implements IUnitOfMeasurePersistenceMapper {
    @Override
    public UnitOfMeasureEntity toUnitOfMeasureEntity(UnitOfMeasure unitOfMeasure) {
        if (unitOfMeasure == null) {
            return null;
        }

        UnitOfMeasureEntity unitOfMeasureEntity = new UnitOfMeasureEntity();

        unitOfMeasureEntity.setId(unitOfMeasure.getId());
        unitOfMeasureEntity.setName(unitOfMeasure.getName());
        unitOfMeasureEntity.setDescription(unitOfMeasure.getDescription());
        unitOfMeasureEntity.setAbbreviation(unitOfMeasure.getAbbreviation());

        return unitOfMeasureEntity;
    }

    @Override
    public UnitOfMeasure toUnitOfMeasure(UnitOfMeasureEntity unitOfMeasureEntity) {
        if (unitOfMeasureEntity == null) {
            return null;
        }

        UnitOfMeasure.UnitOfMeasureBuilder unitOfMeasureBuilder = UnitOfMeasure.builder();

        unitOfMeasureBuilder.id(unitOfMeasureEntity.getId());
        unitOfMeasureBuilder.name(unitOfMeasureEntity.getName());
        unitOfMeasureBuilder.description(unitOfMeasureEntity.getDescription());
        unitOfMeasureBuilder.abbreviation(unitOfMeasureEntity.getAbbreviation());

        return unitOfMeasureBuilder.build();
    }

    @Override
    public List<UnitOfMeasure> toUnitOfMeasureList(List<UnitOfMeasureEntity> unitOfMeasureEntityList) {
        if ( unitOfMeasureEntityList == null ) {
            return null;
        }

        List<UnitOfMeasure> list = new ArrayList<UnitOfMeasure>( unitOfMeasureEntityList.size() );
        for ( UnitOfMeasureEntity unitOfMeasureEntity : unitOfMeasureEntityList ) {
            list.add( toUnitOfMeasure( unitOfMeasureEntity ) );
        }

        return list;
    }
}
