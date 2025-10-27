package com.products_management.application.ports.output;

import com.products_management.domain.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define los puertos de persistencia para el servicio de categorías.
 * Los puertos de persistencia representan las operaciones de almacenamiento y recuperación
 * de datos relacionadas con las categorías.
 */
public interface ICategoryPersistencePort {

    /**
     * Busca una categoría por su ID y empresa.
     *
     * @param id el ID de la categoría a buscar.
     * @param enterpriseId el ID de la empresa.
     * @return un Optional que contiene la categoría encontrada, o un Optional vacío si no se encuentra.
     */
    Optional<Category> findByIdAndEnterpriseId(Long id, String enterpriseId);

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
     * Busca categorías activas por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param state el estado de la categoría.
     * @return una lista de categorías activas de la empresa.
     */
    List<Category> findByEnterpriseIdAndState(String enterpriseId, boolean state);

    /**
     * Obtiene todas las categorías de una empresa con paginación.
     * @param enterpriseId El id de la empresa
     * @param pageable El objeto pageable
     * @return La página de categorías
     */
    Page<Category> getAllCategoriesBy(String enterpriseId, Pageable pageable);

    /**
     * Obtiene todas las categorías de una empresa filtradas por estado.
     * Optimizado para exportación con filtro de estado en BD.
     *
     * @param enterpriseId El identificador de la entidad
     * @param state Estado de las categorías (true=activas, false=inactivas)
     * @param pageable El objeto Pageable que contiene la información de paginación
     * @return Una página de objetos Category filtrados por estado
     */
    Page<Category> getAllCategoriesByState(String enterpriseId, Boolean state, Pageable pageable);

    /**
     * Busca categorías por empresa y término de búsqueda.
     * Busca en: nombres, descripción.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @param pageable Paginación con ordenamiento
     * @return Página de categorías que coinciden con la búsqueda
     */
    Page<Category> findByEnterpriseIdAndSearch(String enterpriseId, String search, Pageable pageable);

    /**
     * Cuenta categorías por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de categorías que coinciden
     */
    long countByEnterpriseIdAndSearch(String enterpriseId, String search);

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
     * Cuenta el total de categorías por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de categorías
     */
    long countByEnterpriseId(String enterpriseId);

    /**
     * Obtiene todas las categorías activas de una empresa con ordenamiento.
     *
     * @param enterpriseId ID de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías activas
     */
    Page<Category> getActiveCategoriesBy(String enterpriseId, int page, int size, String sortField, String sortOrder);

    /**
     * Cuenta el total de categorías activas por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad total de categorías activas
     */
    long countActiveByEnterpriseId(String enterpriseId);
    
    /**
     * Verifica si existe una categoría con el nombre especificado para una empresa.
     *
     * @param name el nombre de la categoría.
     * @param enterpriseId el ID de la empresa.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe una categoría con el nombre especificado para una empresa, excluyendo un ID específico.
     *
     * @param name el nombre de la categoría.
     * @param enterpriseId el ID de la empresa.
     * @param id el ID a excluir de la búsqueda.
     * @return true si existe, false en caso contrario.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);
}
