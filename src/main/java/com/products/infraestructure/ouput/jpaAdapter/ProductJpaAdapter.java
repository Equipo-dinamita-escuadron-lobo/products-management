package com.products.infraestructure.ouput.jpaAdapter;

import com.products.aplication.output.IProductOutputPort;
import com.products.domain.models.Product;
import com.products.infraestructure.ouput.jpaAdapter.Entity.ProductEntity;
import com.products.infraestructure.ouput.jpaAdapter.Mapper.IProductMapper;
import com.products.infraestructure.ouput.jpaAdapter.Repository.IProductRepository;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Data
public class ProductJpaAdapter implements IProductOutputPort {

    private final IProductRepository productRepository;
    private final IProductMapper productMapper;

    @Override
    public Product getById(String id) {
        ProductEntity productEntity = productRepository.findById(id).get();
        return productMapper.toDomain(productEntity);
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
    @Override
    public List<Product> getAllProduct() {
        List<ProductEntity> productEntities = productRepository.findAll();
        return productMapper.toDomainList(productEntities);
    }
}
