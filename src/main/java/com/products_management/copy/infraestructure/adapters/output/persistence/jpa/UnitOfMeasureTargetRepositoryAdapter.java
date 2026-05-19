package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.IUnitOfMeasureTargetRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import com.products_management.infraestructure.output.persistence.repository.IUnitOfMeasureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador JPA para insertar UnitOfMeasure en entDestino.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class UnitOfMeasureTargetRepositoryAdapter implements IUnitOfMeasureTargetRepositoryPort {

    private final IUnitOfMeasureRepository repository;

    @Override
    @Transactional
    public UnitOfMeasureEntity guardar(UnitOfMeasureEntity entity) {
        return repository.save(entity);
    }
}
