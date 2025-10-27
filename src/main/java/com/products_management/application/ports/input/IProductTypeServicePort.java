package com.products_management.application.ports.input;

import com.products_management.domain.exception.productType.ProductTypeNotFoundException;
import com.products_management.domain.model.ProductType;
import java.util.List;
import java.util.Optional;



public interface IProductTypeServicePort {
    ProductType createProductType(ProductType productType);

    Optional<ProductType> findById(Long id);
    
    /**
     * Busca un tipo de producto por ID y empresa, lanza excepción si no se encuentra.
     * 
     * @param id el ID del tipo de producto a buscar
     * @param enterpriseId el ID de la empresa
     * @return el tipo de producto encontrado
     * @throws ProductTypeNotFoundException si no se encuentra el tipo de producto
     */
    ProductType getProductTypeByIdAndEnterpriseId(Long id, String enterpriseId);

    List<ProductType> getProductTypesByEnterpriseId(String enterpriseId);

    /**
     * Obtiene una lista de todos los tipos de producto activados asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los tipos de producto activados de la empresa.
     */
    List<ProductType> findActivated(String enterpriseId);

    ProductType updateProductType(Long id, String enterpriseId, ProductType productType);

    void deleteProductType(Long id, String enterpriseId);
    
    /**
     * Cambia el estado de un tipo de producto (activado/desactivado).
     *
     * @param id el ID del tipo de producto cuyo estado se va a cambiar.
     * @param enterpriseId el ID de la empresa.
     */
    void changeState(Long id, String enterpriseId);
    
    /**
     * Busca tipos de producto activos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param state el estado del tipo de producto.
     * @return una lista de tipos de producto activos de la empresa.
     */
    List<ProductType> getProductTypesByEnterpriseIdAndState(String enterpriseId, boolean state);
}
