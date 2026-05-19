package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.ICategoryTargetRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import com.products_management.infraestructure.output.persistence.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador JPA para insertar Category en entDestino.
 * El tenant override debe estar activo (TenantContext.setTenantId) antes de llamar.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class CategoryTargetRepositoryAdapter implements ICategoryTargetRepositoryPort {

    private final ICategoryRepository repository;

    @Override
    @Transactional
    public CategoryEntity guardar(CategoryEntity entity) {
        return repository.save(entity);
    }
}
