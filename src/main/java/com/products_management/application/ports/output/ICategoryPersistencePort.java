package com.products_management.application.ports.output;

import com.products_management.domain.model.Category;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los puertos de persistencia para el servicio de categorías.
 * Los puertos de persistencia representan las operaciones de almacenamiento y recuperación
 * de datos relacionadas con las categorías.
 */
public interface ICategoryPersistencePort {

    /**
     * Busca una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar.
     * @return un Optional que contiene la categoría encontrada, o un Optional vacío si no se encuentra.
     */
    Optional<Category> findById(Long id);

    /**
     * Obtiene una lista de todas las categorías.
     *
     * @return una lista de todas las categorías.
     */
    List<Category> findAll();

    /**
     * Crea una nueva categoría.
     *
     * @param category la categoría a crear.
     * @return la categoría creada.
     */
    Category create(Category category);

    /**
     * Elimina una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar.
     */
    void deleteById(Long id);

    /**
     * Elimina todas las categorías.
     */
    void deleteAll();
}
