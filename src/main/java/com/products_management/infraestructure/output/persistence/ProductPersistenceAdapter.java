package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.mapper.IProductPersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductPersistenceMapper productPersistenceMapper;

    @Override
    public Optional<Product> findByCodeProduct(String code) {
        return productRepository.findById(Long.valueOf(code))
                .map(productPersistenceMapper::toProduct);
    }

    @Override
    public List<Product> findAllProducts() {
        return productPersistenceMapper.toProductList(productRepository.findAll());
    }

    @Override
    public Product createProduct(Product product) {
        return productPersistenceMapper.toProduct(productRepository.save(productPersistenceMapper.toProductEntity(product)));
    }

    @Override
    public void deleteByCodeProduct(String code) {
        productRepository.deleteById(Long.valueOf(code));
    }
}
