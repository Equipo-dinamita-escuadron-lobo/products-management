package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;

import java.util.List;

public interface IProductPersistenceMapper {

    ProductEntity toProductEntity(Product product);

    Product toProduct(ProductEntity productEntity);

    List<Product> toProductList(List<ProductEntity> productList);
}
