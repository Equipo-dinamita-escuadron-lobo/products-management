package com.products_management.infraestructure.output.persistence.mapper.impl;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductPersistenceMapper;

import org.apache.commons.lang3.ObjectUtils.Null;
import org.springframework.stereotype.Component;


@Component
public class ProductPersistenceMapperImpl implements IProductPersistenceMapper {

    @Override
    public ProductEntity toProductEntity(Product product) {
        if (product == null) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();
        
        productEntity.setId(product.getId());
        productEntity.setCode(product.getCode());
        productEntity.setItemType(product.getItemType());
        productEntity.setDescription(product.getDescription());
        productEntity.setMinQuantity(product.getMinQuantity());
        productEntity.setMaxQuantity(product.getMaxQuantity());
        productEntity.setTaxPercentage(product.getTaxPercentage());
        productEntity.setCreationDate(product.getCreationDate());
        productEntity.setUnitOfMeasureId(product.getUnitOfMeasureId());
        productEntity.setSupplierId(product.getSupplierId());
        productEntity.setCategoryId(product.getCategoryId());
        productEntity.setEnterpriseId(product.getEnterpriseId());        
        productEntity.setPrice(product.getPrice());
        if(product.getState()==null)
            product.setState("true");
        productEntity.setState(product.getState());
        return productEntity;
    }

    @Override
    public Product toProduct(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }

        Product.ProductBuilder productBuilder = Product.builder();

        productBuilder.id(productEntity.getId());
        productBuilder.code(productEntity.getCode());
        productBuilder.itemType(productEntity.getItemType());
        productBuilder.description(productEntity.getDescription());
        productBuilder.minQuantity(productEntity.getMinQuantity());
        productBuilder.maxQuantity(productEntity.getMaxQuantity());
        productBuilder.taxPercentage(productEntity.getTaxPercentage());
        productBuilder.creationDate(productEntity.getCreationDate());
        productBuilder.unitOfMeasureId(productEntity.getUnitOfMeasureId());
        productBuilder.supplierId(productEntity.getSupplierId());
        productBuilder.categoryId(productEntity.getCategoryId());
        productBuilder.enterpriseId(productEntity.getEnterpriseId());
        productBuilder.price(productEntity.getPrice());
        productBuilder.state(productEntity.getState());

        return productBuilder.build();
    }


    @Override
    public List<Product> toProductList(List<ProductEntity> productList) {
        if ( productList == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( productList.size() );
        for ( ProductEntity productEntity : productList ) {
            list.add( toProduct( productEntity ) );
        }

        return list;
    }
}
