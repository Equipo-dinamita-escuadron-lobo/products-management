package com.products_management.infraestructure.output.persistence.mapper.impl;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductTypePersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del mapper de persistencia para la entidad ProductType.
 */
@Component
public class ProductTypePersistenceMapperImpl implements IProductTypePersistenceMapper {

    @Override
    public ProductTypeEntity toProductTypeEntity(ProductType productType) {
        if (productType == null) {
            return null;
        }

        ProductTypeEntity productTypeEntity = new ProductTypeEntity();
        productTypeEntity.setId(productType.getId());
        productTypeEntity.setName(productType.getName());
        productTypeEntity.setDescription(productType.getDescription());
        productTypeEntity.setEnterpriseId(productType.getEnterpriseId());

        return productTypeEntity;
    }

    @Override
    public ProductType toProductType(ProductTypeEntity productTypeEntity) {
        if (productTypeEntity == null) {
            return null;
        }

        return ProductType.builder()
                .id(productTypeEntity.getId())
                .name(productTypeEntity.getName())
                .description(productTypeEntity.getDescription())
                .enterpriseId(productTypeEntity.getEnterpriseId())
                .build();
    }

    @Override
    public List<ProductType> toProductTypeList(List<ProductTypeEntity> productTypeEntityList) {
        if (productTypeEntityList == null) {
            return null;
        }

        List<ProductType> productTypeList = new ArrayList<>(productTypeEntityList.size());
        for (ProductTypeEntity productTypeEntity : productTypeEntityList) {
            productTypeList.add(toProductType(productTypeEntity));
        }

        return productTypeList;
    }
}
