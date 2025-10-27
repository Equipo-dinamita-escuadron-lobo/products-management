package com.products_management.application.ports.input;

import com.products_management.domain.exception.productType.ProductTypeNotFoundException;
import com.products_management.domain.model.ProductType;
import org.springframework.data.domain.Page;
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

    /**
     * Obtiene todos los tipos de producto de una empresa con paginación y ordenamiento.
     *
     * @param enterpriseId ID de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo por el que ordenar (solo "name")
     * @param sortOrder Orden (asc/desc)
     * @return Página de tipos de producto
     */
    Page<ProductType> getAllProductTypesByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder);

    /**
     * Busca tipos de producto por empresa y término de búsqueda con paginación.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo por el que ordenar (solo "name")
     * @param sortOrder Orden (asc/desc)
     * @return Página de tipos de producto que coinciden con la búsqueda
     */
    Page<ProductType> findByEnterpriseIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta tipos de producto por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de tipos de producto que coinciden
     */
    long countByEnterpriseIdAndSearch(String enterpriseId, String search);

    /**
     * Cuenta todos los tipos de producto de una empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad total de tipos de producto
     */
    long countByEnterpriseId(String enterpriseId);
}
