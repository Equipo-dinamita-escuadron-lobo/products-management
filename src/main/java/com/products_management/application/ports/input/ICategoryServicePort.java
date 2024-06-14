package com.products_management.application.ports.input;

import com.products_management.domain.model.Category;

import java.util.List;

/**
 * Interfaz que define los puertos de entrada para el servicio de categorías.
 * Los puertos de entrada representan las operaciones que pueden ser realizadas
 * sobre las categorías en la aplicación.
 */
public interface ICategoryServicePort {

    /**
     * Busca una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar.
     * @return la categoría encontrada, o null si no se encuentra.
     */
    Category findById(Long id);

    /**
     * Obtiene una lista de todas las categorías asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las categorías de la empresa.
     */
    List<Category> findAll(String enterpriseId);

    /**
     * Obtiene una lista de todas las categorías activadas asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las categorías activadas de la empresa.
     */
    List<Category> findActivated(String enterpriseId);

    /**
     * Crea una nueva categoría.
     *
     * @param category la categoría a crear.
     * @return la categoría creada.
     */
    Category create(Category category);

    /**
     * Actualiza una categoría existente.
     *
     * @param id el ID de la categoría a actualizar.
     * @param category los datos de la categoría actualizada.
     * @return la categoría actualizada.
     */
    Category update(Long id, Category category);

    /**
     * Elimina una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar.
     */
    void deleteById(Long id);

    /**
     * Cambia el estado de una categoría (por ejemplo, activado/desactivado).
     *
     * @param id el ID de la categoría cuyo estado se va a cambiar.
     */
    void changeState(Long id);

    /**
     * Elimina todas las categorías.
     */
    void deleteAll();
}
