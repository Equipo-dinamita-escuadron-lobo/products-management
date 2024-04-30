package com.products_management.application.ports.output;

import com.products_management.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductPersistencePort {
    Optional<Product> findById(Long id);
    List<Product> findAll();
    Product create(Product product);
    void deleteById(Long id);
    void deleteAll();
}
