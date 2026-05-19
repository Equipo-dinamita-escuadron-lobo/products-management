package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.IProductTypeTargetRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;
import com.products_management.infraestructure.output.persistence.repository.IProductTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador JPA para insertar ProductType en entDestino.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class ProductTypeTargetRepositoryAdapter implements IProductTypeTargetRepositoryPort {

    private final IProductTypeRepository repository;

    @Override
    @Transactional
    public ProductTypeEntity guardar(ProductTypeEntity entity) {
        return repository.save(entity);
    }
}
