package com.products_management.application.ports.output;

import com.products_management.domain.model.Product;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los puertos de persistencia para el servicio de productos.
 * Los puertos de persistencia representan las operaciones de almacenamiento y recuperación
 * de datos relacionadas con los productos.
 */
public interface IProductPersistencePort {

    /**
     * Busca un producto por su ID.
     *
     * @param id el ID del producto a buscar.
     * @return un Optional que contiene el producto encontrado, o un Optional vacío si no se encuentra.
     */
    Optional<Product> findById(Long id);

    /**
     * Busca un producto por su ID y empresa.
     *
     * @param id el ID del producto a buscar.
     * @param enterpriseId el ID de la empresa.
     * @return un Optional que contiene el producto encontrado, o un Optional vacío si no se encuentra.
     */
    Optional<Product> findByIdAndEnterpriseId(Long id, String enterpriseId);

    /**
     * Obtiene una lista de todos los productos.
     *
     * @return una lista de todos los productos.
     */
    List<Product> findAll();

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
     * Busca productos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de productos de la empresa.
     */
    List<Product> findByEnterpriseId(String enterpriseId);    /**
     * Busca productos activos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param state el estado del producto.
     * @return una lista de productos activos de la empresa.
     */
    List<Product> findByEnterpriseIdAndState(String enterpriseId, boolean state);
    
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
}
