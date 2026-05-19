package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.IUnitOfMeasureSourceRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Adaptador JPA para el puerto de lectura de UnitOfMeasure en entOrigen.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class UnitOfMeasureSourceRepositoryAdapter implements IUnitOfMeasureSourceRepositoryPort {

    private final UnitOfMeasureCopySourceRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<UnitOfMeasureEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte) {
        return repository.findByEntOrigenBeforeSnapshot(entOrigen, snapshotCorte);
    }
}
