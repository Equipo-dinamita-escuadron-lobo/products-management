package com.products_management.application.ports.input;

import com.products_management.domain.exception.ProductTypeNotFoundException;
import com.products_management.domain.model.ProductType;
import java.util.List;
import java.util.Optional;



public interface IProductTypeServicePort {
    ProductType createProductType(ProductType productType);

    Optional<ProductType> findById(Long id);
    
    /**
     * Busca un tipo de producto por ID y lanza excepción si no se encuentra.
     * 
     * @param id el ID del tipo de producto a buscar
     * @return el tipo de producto encontrado
     * @throws ProductTypeNotFoundException si no se encuentra el tipo de producto
     */
    ProductType getProductTypeById(Long id);

    List<ProductType> getProductTypesByEnterpriseId(String enterpriseId);

    List<ProductType> listAllProductTypes();

    ProductType updateProductType(Long id, ProductType productType);

    void deleteProductType(Long id);
}
