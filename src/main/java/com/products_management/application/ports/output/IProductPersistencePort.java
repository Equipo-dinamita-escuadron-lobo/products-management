package com.products_management.application.ports.output;

import com.products_management.domain.model.Product;

import java.util.List;
import java.util.Optional;

public interface IProductPersistencePort {
    Optional<Product> findByCodeProduct(String code);
    List<Product> findAllProducts();
    Product createProduct(Product product);
    void deleteByCodeProduct(String code);
}
