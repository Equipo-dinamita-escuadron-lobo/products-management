package com.products_management.application.ports.output;

import com.products_management.domain.model.ProductType;
import org.springframework.data.domain.Page;
import java.util.Optional;

public interface  IProductTypePersistencePort {

    ProductType save(ProductType productType);

    /**
     * Busca un tipo de producto por ID e ID de empresa.
     */
    Optional<ProductType> findByIdAndEnterpriseId(Long id, String enterpriseId);
    
    Optional<ProductType> findById(Long id);    ProductType update(Long id, ProductType productType);
    
    void delete(Long id);
    
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

    /**
     * Obtiene todos los tipos de producto de una empresa con paginación y ordenamiento.
     *
     * @param enterpriseId ID de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo por el que ordenar
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
     * @param sortField Campo por el que ordenar
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

    /**
     * Busca tipos de producto activados por empresa con paginación.
     *
     * @param enterpriseId ID de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @return Página de tipos de producto activados
     */
    Page<ProductType> findActivatedByEnterpriseId(String enterpriseId, int page, int size);

    /**
     * Cuenta tipos de producto activados por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad de tipos de producto activados
     */
    long countActivatedByEnterpriseId(String enterpriseId);
}
