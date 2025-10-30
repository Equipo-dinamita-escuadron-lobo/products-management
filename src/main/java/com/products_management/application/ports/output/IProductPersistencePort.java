package com.products_management.application.ports.output;

import com.products_management.domain.model.Product;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;

/**
 * Interfaz que define los puertos de persistencia para el servicio de productos.
 * Los puertos de persistencia representan las operaciones de almacenamiento y recuperación
 * de datos relacionadas con los productos.
 */
public interface IProductPersistencePort {

    /**
     * Busca un producto por su ID y empresa.
     *
     * @param id el ID del producto a buscar.
     * @param enterpriseId el ID de la empresa.
     * @return un Optional que contiene el producto encontrado, o un Optional vacío si no se encuentra.
     */
    Optional<Product> findByIdAndEnterpriseId(Long id, String enterpriseId);

    /**
     * Crea un nuevo producto.
     *
     * @param product el producto a crear.
     * @return el producto creado.
     */
    Product create(Product product);

    /**
     * Elimina un producto por su ID.
     *
     * @param id el ID del producto a eliminar.
     */
    void deleteById(Long id);

    /**
     * Busca productos activos por ID de empresa con paginación.
     *
     * @param enterpriseId el ID de la empresa.
     * @param pageNumber el número de página.
     * @param pageSize el tamaño de página.
     * @return una página de productos activos.
     */
    Page<Product> findActivatedWithPagination(String enterpriseId, int pageNumber, int pageSize);
    
    /**
     * Busca productos por ID de categoría.
     *
     * @param categoryId el ID de la categoría.
     * @return una lista de productos de la categoría.
     */
    List<Product> findByCategoryId(Long categoryId);
    
    /**
     * Busca productos por ID de unidad de medida.
     *
     * @param unitOfMeasureId el ID de la unidad de medida.
     * @return una lista de productos con esa unidad de medida.
     */
    List<Product> findByUnitOfMeasureId(Long unitOfMeasureId);
    
    /**
     * Busca productos por ID de tipo de producto.
     *
     * @param productTypeId el ID del tipo de producto.
     * @return una lista de productos de ese tipo de producto.
     */
    List<Product> findByProductTypeId(Long productTypeId);
    
    /**
     * Verifica si existe un producto con el nombre especificado para una empresa.
     *
     * @param name el nombre del producto.
     * @param enterpriseId el ID de la empresa.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe un producto con la referencia especificada para una empresa.
     *
     * @param reference la referencia del producto.
     * @param enterpriseId el ID de la empresa.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByReferenceAndEnterpriseId(String reference, String enterpriseId);
    
    /**
     * Verifica si existe un producto con el nombre especificado para una empresa, excluyendo un ID específico.
     *
     * @param name el nombre del producto.
     * @param enterpriseId el ID de la empresa.
     * @param id el ID a excluir de la búsqueda.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);
    
    /**
     * Verifica si existe un producto con la referencia especificada para una empresa, excluyendo un ID específico.
     *
     * @param reference la referencia del producto.
     * @param enterpriseId el ID de la empresa.
     * @param id el ID a excluir de la búsqueda.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByReferenceAndEnterpriseIdAndIdNot(String reference, String enterpriseId, Long id);
    
    /**
     * Busca productos por ID de empresa con filtros de búsqueda y paginación.
     *
     * @param enterpriseId el ID de la empresa.
     * @param search el término de búsqueda (opcional).
     * @param pageNumber el número de página.
     * @param pageSize el tamaño de página.
     * @param sortField el campo de ordenamiento.
     * @param sortOrder el orden (asc/desc).
     * @return una página de productos.
     */
    Page<Product> findByEnterpriseIdWithFilters(String enterpriseId, String search, int pageNumber, int pageSize, String sortField, String sortOrder);
    
    /**
     * Cuenta productos por ID de empresa con filtros de búsqueda.
     *
     * @param enterpriseId el ID de la empresa.
     * @param search el término de búsqueda (opcional).
     * @return el número de productos que coinciden.
     */
    long countByEnterpriseIdWithFilters(String enterpriseId, String search);
    
    /**
     * Cuenta todos los productos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return el número total de productos.
     */
    long countByEnterpriseId(String enterpriseId);
    
    /**
     * Cuenta productos activos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return el número de productos activos.
     */
    long countActivatedByEnterpriseId(String enterpriseId);
}
