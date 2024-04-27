package com.products_management.application.ports.output;

import com.products_management.domain.model.UnitOfMeasure;
import java.util.List;
import java.util.Optional;

public interface IUnitOfMeasurePersistencePort {
    Optional<UnitOfMeasure> findById(Long id);
    List<UnitOfMeasure> findAll();
    UnitOfMeasure create(UnitOfMeasure unitOfMeasure);
    void deleteById(Long id);
}
