package com.products_management.infraestructure.input.rest.mapper.impl;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.input.rest.model.request.ProductTypeRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductTypeResponse;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductTypeRestMapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

/**
 * Implementación del mapper para ProductType.
 */
@Component
public class ProductTypeMapperImpl implements IProductTypeRestMapper {

    @Override
    public ProductType toProductType(ProductTypeRequest request) {
        if (request == null) {
            return null;
        }

        ProductType productType = new ProductType();
        productType.setName(request.getName());
        productType.setDescription(request.getDescription());
        productType.setEnterpriseId(request.getEnterpriseId());

        return productType;
    }


    @Override
    public List<ProductType> toProductTypeList(List<ProductTypeRequest> productTypeRequestList) {
        if (productTypeRequestList == null) {
            return null;
        }

        List<ProductType> productTypeList = new ArrayList<>(productTypeRequestList.size());
        for (ProductTypeRequest request : productTypeRequestList) {
            ProductType productType = toProductType(request);
            if (productType != null) {
                productTypeList.add(productType);
            }
        }

        return productTypeList;
    }


    @Override
    public ProductTypeResponse toProductTypeResponse(ProductType productType) {
        if (productType == null) {
            return null;
        }
    
        ProductTypeResponse response = new ProductTypeResponse();
        response.setId(productType.getId());
        response.setName(productType.getName());
        response.setDescription(productType.getDescription());
        response.setEnterpriseId(productType.getEnterpriseId());
    
        return response;
    }
    


}
