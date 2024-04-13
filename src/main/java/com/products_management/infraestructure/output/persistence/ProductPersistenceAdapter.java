package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.mapper.IProductPersistenceMapperImpl;
import com.products_management.infraestructure.output.persistence.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductPersistenceMapperImpl productPersistenceMapper;

    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(Long.valueOf(id))
                .map(productPersistenceMapper::toProduct);
    }

    @Override
    public List<Product> findAll() {
        return productPersistenceMapper.toProductList(productRepository.findAll());
    }

    @Override
    public Product create(Product product) {
        return productPersistenceMapper.toProduct(productRepository.save(productPersistenceMapper.toProductEntity(product)));
    }

    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(Long.valueOf(id));
    }
}
