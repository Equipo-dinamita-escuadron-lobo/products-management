package com.products_management.application.ports.input;

import com.products_management.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Interfaz que define los puertos de entrada para el servicio de categorías.
 * Los puertos de entrada representan las operaciones que pueden ser realizadas
 * sobre las categorías en la aplicación.
 */
public interface ICategoryServicePort {

    /**
     * Busca una categoría por su ID y empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría a buscar.
     * @return la categoría encontrada.
     */
    Category findById(String enterpriseId, Long id);

    /**
     * Obtiene todas las categorías de una empresa con paginación.
     * @param enterpriseId El id de la empresa
     * @param pageable El objeto pageable
     * @return La página de categorías
     */
    Page<Category> getAllCategoriesBy(String enterpriseId, Pageable pageable);

    /**
     * Cuenta el total de categorías por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de categorías
     */
    long countAllCategoriesByEntId(String enterpriseId);

    /**
     * Busca categorías por empresa y término de búsqueda con ordenamiento.
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías que coinciden con la búsqueda
     */
    Page<Category> findByEntIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta categorías por empresa y término de búsqueda.
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de categorías que coinciden
     */
    long countByEntIdAndSearch(String enterpriseId, String search);

    /**
     * Obtiene todas las categorías con ordenamiento.
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías ordenadas
     */
    Page<Category> getAllCategoriesByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder);

    /**
     * Obtiene todas las categorías activas con ordenamiento.
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías activas ordenadas
     */
    Page<Category> getAllActiveCategoriesByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta el total de categorías activas por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de categorías activas
     */
    long countActiveCategoriesByEntId(String enterpriseId);


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
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría a actualizar.
     * @param category los datos de la categoría actualizada.
     * @return la categoría actualizada.
     */
    Category update(String enterpriseId, Long id, Category category);

    /**
     * Elimina una categoría por su ID y empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría a eliminar.
     */
    void deleteById(String enterpriseId, Long id);

    /**
     * Cambia el estado de una categoría (por ejemplo, activado/desactivado).
     *
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría cuyo estado se va a cambiar.
     */
    void changeState(String enterpriseId, Long id);
}
