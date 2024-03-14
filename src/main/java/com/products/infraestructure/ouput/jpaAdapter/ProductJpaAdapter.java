package com.products.infraestructure.ouput.jpaAdapter;

import org.springframework.stereotype.Component;

import com.products.aplication.output.IProductCreateOutputPort;
import com.products.aplication.output.IProductSearchOutputPort;
import com.products.domain.models.Product;
import com.products.infraestructure.ouput.jpaAdapter.Entity.ProductEntity;
import com.products.infraestructure.ouput.jpaAdapter.Mapper.IProductMapper;
import com.products.infraestructure.ouput.jpaAdapter.Repository.IProductRepository;

import lombok.Data;

@Component
@Data
public class ProductJpaAdapter implements IProductSearchOutputPort, IProductCreateOutputPort {

    private final IProductRepository productRepository;
    private final IProductMapper productMapper;

    @Override
    public Product getById(String id) {
        ProductEntity productEntity = productRepository.findById(id).get();
        Product product = productMapper.toDomain(productEntity);
        return product;
    }
    
    @Override
    public Product createProduct(Product product) {
        if (product == null) {
            return null;
        }
        ProductEntity productEntity = productMapper.toEntity(product);
        productEntity = productRepository.save(productEntity);
        return productMapper.toDomain(productEntity);
    }
}
