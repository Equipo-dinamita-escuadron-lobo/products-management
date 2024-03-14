package com.products.infraestructure.ouput.jpaAdapter.Mapper;

import org.mapstruct.Mapper;

import com.products.domain.models.Product;
import com.products.infraestructure.ouput.jpaAdapter.Entity.ProductEntity;

@Mapper
public interface IProductMapper {
    public Product toDomain(ProductEntity productEntity);
    public ProductEntity toEntity(Product product);    
}
