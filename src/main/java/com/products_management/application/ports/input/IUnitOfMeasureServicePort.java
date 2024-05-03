package com.products_management.application.ports.input;

import com.products_management.domain.model.UnitOfMeasure;
import java.util.List;

public interface IUnitOfMeasureServicePort {
    UnitOfMeasure findById(Long id);
    List<UnitOfMeasure> findAll(String enterpriseId);
    List<UnitOfMeasure> findActivated(String enterpriseId);
    UnitOfMeasure create(UnitOfMeasure unitOfMeasure);
    UnitOfMeasure update(Long id, UnitOfMeasure unitOfMeasure);
    void deleteById(Long id);
    void changeState(Long id);
    void deleteAll();
}
