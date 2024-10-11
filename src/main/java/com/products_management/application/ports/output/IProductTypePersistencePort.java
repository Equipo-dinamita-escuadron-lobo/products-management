package com.products_management.application.ports.output;

import com.products_management.domain.model.ProductType;
import java.util.List;
import java.util.Optional;

public interface  IProductTypePersistencePort {

    ProductType save(ProductType productType);

    List<ProductType> findByEnterpriseId(String enterpriseId);
    
    List<ProductType> findAll();

    Optional<ProductType> findById(Long id);

    ProductType update(Long id, ProductType productType);
    
    void delete(Long id);
}
