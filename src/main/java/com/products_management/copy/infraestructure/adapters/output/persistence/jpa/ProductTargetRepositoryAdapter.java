package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.IProductTargetRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import com.products_management.infraestructure.output.persistence.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador JPA para insertar Product en entDestino.
 * El tenant override debe estar activo antes de llamar.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class ProductTargetRepositoryAdapter implements IProductTargetRepositoryPort {

    private final IProductRepository repository;

    @Override
    @Transactional
    public ProductEntity guardar(ProductEntity entity) {
        return repository.save(entity);
    }
}
