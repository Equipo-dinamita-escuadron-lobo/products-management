package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;

import java.util.List;

/**
 * Interfaz para mapear entre entidades de persistencia (UnitOfMeasureEntity) y objetos del dominio (UnitOfMeasure).
 */
public interface IUnitOfMeasurePersistenceMapper {

    /**
     * Convierte un objeto UnitOfMeasure del dominio en una UnitOfMeasureEntity de persistencia.
     * @param unitOfMeasure Objeto UnitOfMeasure del dominio.
     * @return UnitOfMeasureEntity correspondiente.
     */
    UnitOfMeasureEntity toUnitOfMeasureEntity(UnitOfMeasure unitOfMeasure);

    /**
     * Convierte una UnitOfMeasureEntity de persistencia en un objeto UnitOfMeasure del dominio.
     * @param unitOfMeasureEntity UnitOfMeasureEntity de persistencia.
     * @return Objeto UnitOfMeasure correspondiente.
     */
    UnitOfMeasure toUnitOfMeasure(UnitOfMeasureEntity unitOfMeasureEntity);

    /**
     * Convierte una lista de UnitOfMeasureEntity de persistencia en una lista de objetos UnitOfMeasure del dominio.
     * @param unitOfMeasureEntityList Lista de UnitOfMeasureEntity de persistencia.
     * @return Lista de objetos UnitOfMeasure correspondiente.
     */
    List<UnitOfMeasure> toUnitOfMeasureList(List<UnitOfMeasureEntity> unitOfMeasureEntityList);
}
