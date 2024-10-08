package com.products_management.infraestructure.output.persistence.mapper.impl;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

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
        productEntity.setItemType(product.getItemType());
        productEntity.setDescription(product.getDescription());
        productEntity.setQuantity(product.getQuantity());
        productEntity.setTaxPercentage(product.getTaxPercentage());
        productEntity.setCreationDate(product.getCreationDate());
        productEntity.setUnitOfMeasureId(product.getUnitOfMeasureId());
        productEntity.setSupplierId(product.getSupplierId());
        productEntity.setCategoryId(product.getCategoryId());
        productEntity.setEnterpriseId(product.getEnterpriseId());
        productEntity.setCost(product.getCost());
        productEntity.setState(product.getState() == null ? "true" : product.getState());

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

        return Product.builder()
                .id(productEntity.getId())
                .code(productEntity.getCode())
                .itemType(productEntity.getItemType())
                .description(productEntity.getDescription())
                .quantity(productEntity.getQuantity())
                .taxPercentage(productEntity.getTaxPercentage())
                .creationDate(productEntity.getCreationDate())
                .unitOfMeasureId(productEntity.getUnitOfMeasureId())
                .supplierId(productEntity.getSupplierId())
                .categoryId(productEntity.getCategoryId())
                .enterpriseId(productEntity.getEnterpriseId())
                .cost(productEntity.getCost())
                .state(productEntity.getState())
                .build();
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
