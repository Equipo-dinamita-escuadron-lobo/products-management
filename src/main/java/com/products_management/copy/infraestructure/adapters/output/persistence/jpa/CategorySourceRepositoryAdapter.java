package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.ICategorySourceRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Adaptador JPA para el puerto de lectura de Category en entOrigen.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class CategorySourceRepositoryAdapter implements ICategorySourceRepositoryPort {

    private final CategoryCopySourceRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte) {
        return repository.findByEntOrigenBeforeSnapshot(entOrigen, snapshotCorte);
    }
}
