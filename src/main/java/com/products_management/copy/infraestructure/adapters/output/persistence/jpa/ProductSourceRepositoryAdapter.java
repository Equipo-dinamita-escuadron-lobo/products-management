package com.products_management.copy.infraestructure.adapters.output.persistence.jpa;

import com.products_management.copy.application.output.IProductSourceRepositoryPort;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Adaptador JPA para el puerto de lectura de Product en entOrigen.
 * REQ-PRODUCTS-04.
 */
@Component
@RequiredArgsConstructor
public class ProductSourceRepositoryAdapter implements IProductSourceRepositoryPort {

    private final ProductCopySourceRepository repository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductEntity> findByEntOrigenBeforeSnapshot(String entOrigen, Instant snapshotCorte) {
        return repository.findByEntOrigenBeforeSnapshot(entOrigen, snapshotCorte);
    }
}
