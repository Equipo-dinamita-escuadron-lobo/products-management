package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.IProductTypeSourceRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Adaptador JPA para el puerto de lectura de ProductType en entOrigen.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class ProductTypeSourceRepositoryAdapter implements IProductTypeSourceRepositoryPort {

    private final ProductTypeCopySourceRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductTypeEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte) {
        return repository.findByEntOrigenBeforeSnapshot(entOrigen, snapshotCorte);
    }
}
