package com.products_management.application.service;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.exception.UnitOfMeasureAssociatedException;
import com.products_management.domain.exception.UnitOfMeasureNotFoundException;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.UnitOfMeasure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UnitOfMeasureService implements IUnitOfMeasureServicePort {

    private final IUnitOfMeasurePersistencePort unitMeasurePersistencePort;
    private final ProductService productServicePort;    
    


    @Override
    public UnitOfMeasure findById(Long id) {
        return unitMeasurePersistencePort.findById(id).orElseThrow(UnitOfMeasureNotFoundException::new);
    }

    @Override
    public List<UnitOfMeasure> findAll(String enterpriseId) {
        List<UnitOfMeasure> allUnitOfMeasure = unitMeasurePersistencePort.findAll();
        return allUnitOfMeasure.stream()
                .filter(unitOfMeasure -> unitOfMeasure.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());

    }

    @Override
    public List<UnitOfMeasure> findActivated(String enterpriseId) {
        List<UnitOfMeasure> allUnitOfMeasure = unitMeasurePersistencePort.findAll();
        return allUnitOfMeasure.stream()
                .filter(unitOfMeasure -> unitOfMeasure.getState() .equals("true"))
                .filter(unitOfMeasure -> unitOfMeasure.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
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
        List<Product> products = productServicePort.findAllByUnitOfMeasure(id);
        if(!products.isEmpty()){
            throw new UnitOfMeasureAssociatedException();
        }
        unitMeasurePersistencePort.deleteById(id);
    }

    @Override
    public void deleteAll() {
        unitMeasurePersistencePort.deleteAll();
    }
}
