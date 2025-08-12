package com.products_management.application.ports.input;

import com.products_management.domain.exception.productType.ProductTypeNotFoundException;
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

    /**
     * Obtiene una lista de todos los tipos de producto activados asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los tipos de producto activados de la empresa.
     */
    List<ProductType> findActivated(String enterpriseId);

    List<ProductType> listAllProductTypes();

    ProductType updateProductType(Long id, ProductType productType);

    void deleteProductType(Long id);
    
    /**
     * Cambia el estado de un tipo de producto (activado/desactivado).
     *
     * @param id el ID del tipo de producto cuyo estado se va a cambiar.
     */
    void changeState(Long id);
    
    /**
     * Busca tipos de producto activos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param state el estado del tipo de producto.
     * @return una lista de tipos de producto activos de la empresa.
     */
    List<ProductType> getProductTypesByEnterpriseIdAndState(String enterpriseId, boolean state);
}
