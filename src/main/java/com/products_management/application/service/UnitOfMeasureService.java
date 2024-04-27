package com.products_management.application.service;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.exception.UnitOfMeasureNotFoundException;
import com.products_management.domain.model.UnitOfMeasure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UnitOfMeasureService implements IUnitOfMeasureServicePort {
    private final IUnitOfMeasurePersistencePort unitMeasurePersistencePort;


    @Override
    public UnitOfMeasure findById(Long id) {
        return unitMeasurePersistencePort.findById(id).orElseThrow(UnitOfMeasureNotFoundException::new);
    }

    @Override
    public List<UnitOfMeasure> findAll() {
        return unitMeasurePersistencePort.findAll();
    }

    @Override
    public UnitOfMeasure create(UnitOfMeasure unitOfMeasure) {
        return unitMeasurePersistencePort.create(unitOfMeasure);
    }

    @Override
    public UnitOfMeasure update(Long id, UnitOfMeasure unitOfMeasure) {
        return unitMeasurePersistencePort.findById(id)
                .map(create ->{
                    create.setName(unitOfMeasure.getName());
                    create.setDescription(unitOfMeasure.getDescription());
                    create.setAbbreviation(unitOfMeasure.getAbbreviation());
                    return unitMeasurePersistencePort.create(create);
                })
                .orElseThrow(UnitOfMeasureNotFoundException::new);
    }

    @Override
    public void changeState(Long id) {
    UnitOfMeasure unitOfMeasure = unitMeasurePersistencePort.findById(id)
                .orElseThrow(() -> new UnitOfMeasureNotFoundException());
        if ("true".equals(unitOfMeasure.getState())) {
            unitOfMeasure.setState("false"); 
        } else {
            unitOfMeasure.setState("true"); 
        }
        unitMeasurePersistencePort.create(unitOfMeasure);
    }

    @Override
    public void deleteById(Long id) {
        if(unitMeasurePersistencePort.findById(id).isEmpty()){
            throw new UnitOfMeasureNotFoundException();
        }
        unitMeasurePersistencePort.deleteById(id);
    }

    @Override
    public void deleteAll() {
        unitMeasurePersistencePort.deleteAll();
    }
}
