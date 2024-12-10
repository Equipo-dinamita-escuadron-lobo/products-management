package com.products_management.application.ports.input;

import com.products_management.domain.model.ProductType;
import java.util.List;

import java.util.Optional;



public interface IProductTypeServicePort {
    ProductType createProductType(ProductType productType);

    Optional<ProductType> findById(Long id);

    List<ProductType> getProductTypesByEnterpriseId(String enterpriseId);

    List<ProductType> listAllProductTypes();

    ProductType updateProductType(Long id, ProductType productType);

    void deleteProductType(Long id);
}
