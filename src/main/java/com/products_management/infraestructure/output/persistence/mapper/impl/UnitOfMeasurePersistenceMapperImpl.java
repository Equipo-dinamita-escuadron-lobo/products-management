package com.products_management.infraestructure.output.persistence.mapper.impl;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IUnitOfMeasurePersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del mapper de persistencia para la entidad UnitOfMeasure.
 */
@Component
public class UnitOfMeasurePersistenceMapperImpl implements IUnitOfMeasurePersistenceMapper {

    /**
     * Convierte un objeto UnitOfMeasure del dominio en una UnitOfMeasureEntity de persistencia.
     * @param unitOfMeasure Objeto UnitOfMeasure del dominio.
     * @return UnitOfMeasureEntity correspondiente.
     */
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
        unitOfMeasureEntity.setState(unitOfMeasure.getState() == null ? "true" : unitOfMeasure.getState());
        unitOfMeasureEntity.setEnterpriseId(unitOfMeasure.getEnterpriseId());

        return unitOfMeasureEntity;
    }

    /**
     * Convierte una UnitOfMeasureEntity de persistencia en un objeto UnitOfMeasure del dominio.
     * @param unitOfMeasureEntity UnitOfMeasureEntity de persistencia.
     * @return Objeto UnitOfMeasure correspondiente.
     */
    @Override
    public UnitOfMeasure toUnitOfMeasure(UnitOfMeasureEntity unitOfMeasureEntity) {
        if (unitOfMeasureEntity == null) {
            return null;
        }

        return UnitOfMeasure.builder()
                .id(unitOfMeasureEntity.getId())
                .name(unitOfMeasureEntity.getName())
                .description(unitOfMeasureEntity.getDescription())
                .abbreviation(unitOfMeasureEntity.getAbbreviation())
                .state(unitOfMeasureEntity.getState())
                .enterpriseId(unitOfMeasureEntity.getEnterpriseId())
                .build();
    }

    /**
     * Convierte una lista de UnitOfMeasureEntity de persistencia en una lista de objetos UnitOfMeasure del dominio.
     * @param unitOfMeasureEntityList Lista de UnitOfMeasureEntity de persistencia.
     * @return Lista de objetos UnitOfMeasure correspondiente.
     */
    @Override
    public List<UnitOfMeasure> toUnitOfMeasureList(List<UnitOfMeasureEntity> unitOfMeasureEntityList) {
        if (unitOfMeasureEntityList == null) {
            return null;
        }

        List<UnitOfMeasure> unitOfMeasureList = new ArrayList<>(unitOfMeasureEntityList.size());
        for (UnitOfMeasureEntity unitOfMeasureEntity : unitOfMeasureEntityList) {
            unitOfMeasureList.add(toUnitOfMeasure(unitOfMeasureEntity));
        }

        return unitOfMeasureList;
    }
}
