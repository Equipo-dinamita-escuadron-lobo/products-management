package com.products_management.application.ports.output;

import com.products_management.domain.model.ProductType;
import java.util.List;
import java.util.Optional;

public interface  IProductTypePersistencePort {

    ProductType save(ProductType productType);

    /**
     * Busca un tipo de producto por ID e ID de empresa.
     */
    Optional<ProductType> findByIdAndEnterpriseId(Long id, String enterpriseId);
    
    List<ProductType> findByEnterpriseId(String enterpriseId);
    
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
    
    /**
     * Verifica si existe un tipo de producto con el nombre especificado para una empresa.
     *
     * @param name el nombre del tipo de producto.
     * @param enterpriseId el ID de la empresa.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe un tipo de producto con el nombre especificado para una empresa, excluyendo un ID específico.
     *
     * @param name el nombre del tipo de producto.
     * @param enterpriseId el ID de la empresa.
     * @param id el ID a excluir de la búsqueda.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);
}
