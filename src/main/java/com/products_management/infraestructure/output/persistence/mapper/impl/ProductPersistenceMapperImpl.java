package com.products_management.infraestructure.output.persistence.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductPersistenceMapper;

/**
 * Implementación del mapper de persistencia para la entidad Product.
 */
@Component
public class ProductPersistenceMapperImpl implements IProductPersistenceMapper {

    /**
     * Convierte un objeto Product del dominio en una ProductEntity de persistencia.
     * @param product Objeto Product del dominio.
     * @return ProductEntity correspondiente.
     */
    @Override
    public ProductEntity toProductEntity(Product product) {
        if (product == null) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(product.getId());
        productEntity.setCode(product.getCode());
        productEntity.setName(product.getName());
        productEntity.setDescription(product.getDescription());
        productEntity.setQuantity(product.getQuantity());
        productEntity.setTaxPercentage(product.getTaxPercentage());
        productEntity.setCreationDate(product.getCreationDate());
        productEntity.setUnitOfMeasureId(product.getUnitOfMeasureId());
        productEntity.setCategoryId(product.getCategoryId());
        productEntity.setEnterpriseId(product.getEnterpriseId());
        productEntity.setCost(product.getCost());
        productEntity.setState(product.isState());
        productEntity.setReference(product.getReference());
        productEntity.setProductTypeId(product.getProductTypeId());

        return productEntity;
    }

    /**
     * Convierte una ProductEntity de persistencia en un objeto Product del dominio.
     * @param productEntity ProductEntity de persistencia.
     * @return Objeto Product correspondiente.
     */
    @Override
    public Product toProduct(ProductEntity productEntity) {
        if (productEntity == null) {
            return null;
        }

        Product product = Product.builder()
                .id(productEntity.getId())
                .code(productEntity.getCode())
                .name(productEntity.getName())
                .description(productEntity.getDescription())
                .quantity(productEntity.getQuantity())
                .taxPercentage(productEntity.getTaxPercentage())
                .creationDate(productEntity.getCreationDate())
                .unitOfMeasureId(productEntity.getUnitOfMeasureId())
                .categoryId(productEntity.getCategoryId())
                .enterpriseId(productEntity.getEnterpriseId())
                .cost(productEntity.getCost())
                .state(productEntity.isState())
                .reference(productEntity.getReference())
                .productTypeId(productEntity.getProductTypeId())
                .build();

        return product;
    }
    /**
     * Convierte una lista de ProductEntity de persistencia en una lista de objetos Product del dominio.
     * @param productList Lista de ProductEntity de persistencia.
     * @return Lista de objetos Product correspondiente.
     */
    @Override
    public List<Product> toProductList(List<ProductEntity> productList) {
        if (productList == null) {
            return null;
        }

        List<Product> productListDomain = new ArrayList<>(productList.size());
        for (ProductEntity productEntity : productList) {
            productListDomain.add(toProduct(productEntity));
        }

        return productListDomain;
    }
}
