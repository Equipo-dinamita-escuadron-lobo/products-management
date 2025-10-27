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
     * Busca una categoría por su ID y empresa.
     *
     * @param id el ID de la categoría a buscar.
     * @param enterpriseId el ID de la empresa.
     * @return un Optional que contiene la categoría encontrada, o un Optional vacío si no se encuentra.
     */
    Optional<Category> findByIdAndEnterpriseId(Long id, String enterpriseId);

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
     * Busca categorías por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de categorías de la empresa.
     */
    List<Category> findByEnterpriseId(String enterpriseId);
    
    /**
     * Busca categorías activas por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param state el estado de la categoría.
     * @return una lista de categorías activas de la empresa.
     */
    List<Category> findByEnterpriseIdAndState(String enterpriseId, boolean state);
    
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
