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
    
    /**
     * Busca tipos de producto activos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param state el estado del tipo de producto.
     * @return una lista de tipos de producto activos de la empresa.
     */
    List<ProductType> findByEnterpriseIdAndState(String enterpriseId, boolean state);
}
