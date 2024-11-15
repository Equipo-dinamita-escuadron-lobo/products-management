package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz para el repositorio de unidades de medida que extiende JpaRepository.
 * Proporciona métodos para realizar operaciones CRUD en UnitOfMeasureEntity.
 */
public interface IUnitOfMeasureRepository extends JpaRepository<UnitOfMeasureEntity, Long> {
    
}
