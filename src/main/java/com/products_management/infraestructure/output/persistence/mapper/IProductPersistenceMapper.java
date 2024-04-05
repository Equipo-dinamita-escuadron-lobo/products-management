package com.products_management.infraestructure.output.persistence.mapper;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IProductPersistenceMapper {

    ProductEntity toProductEntity(Product product);
    Product toProduct(ProductEntity productEntity);
    List<Product> toProductList(List<ProductEntity> productList);
}
