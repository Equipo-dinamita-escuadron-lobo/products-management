package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUnitOfMeasureRepository extends JpaRepository<UnitOfMeasureEntity, Long> {
}
