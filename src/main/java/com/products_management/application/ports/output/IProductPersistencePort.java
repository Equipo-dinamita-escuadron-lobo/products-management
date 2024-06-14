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
     * Elimina todos los productos.
     */
    void deleteAll();
}
