package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Interfaz para el repositorio de categorías que extiende JpaRepository.
 * Proporciona métodos para realizar operaciones CRUD en CategoryEntity.
 */
public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
    
    /**
     * Busca categorías por ID de empresa.
     */
    List<CategoryEntity> findByEnterpriseId(String enterpriseId);
    
    /**
     * Busca categorías activas por ID de empresa.
     */
    List<CategoryEntity> findByEnterpriseIdAndState(String enterpriseId, boolean state);
    
    /**
     * Verifica si existe una categoría con el nombre especificado para una empresa.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe una categoría con el nombre especificado para una empresa, excluyendo un ID específico.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);

    /**
     * Busca una categoría por ID y empresa.
     */
    Optional<CategoryEntity> findByIdAndEnterpriseId(Long id, String enterpriseId);

    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId")
    Page<CategoryEntity> getCategoriesBy(String enterpriseId, Pageable page);

    /**
     * Obtiene todas las categorías de una empresa filtradas por estado.
     * Optimizado para exportación masiva con filtro de estado.
     *
     * @param enterpriseId ID de la empresa
     * @param state Estado de las categorías (true=activas, false=inactivas)
     * @param page Paginación
     * @return Página de categorías filtradas por estado
     */
    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND c.state = :state")
    Page<CategoryEntity> getCategoriesByEnterpriseIdAndState(@Param("enterpriseId") String enterpriseId, @Param("state") Boolean state, Pageable page);

    /**
     * Busca categorías por empresa y término de búsqueda.
     * Busca en: nombres, descripción.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @param page Paginación con ordenamiento
     * @return Página de categorías que coinciden con la búsqueda
     */
    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CategoryEntity> findByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId, @Param("search") String search, Pageable page);

    /**
     * Cuenta categorías por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de categorías que coinciden
     */
    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    long countByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId, @Param("search") String search);

    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId")
    long countByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * Obtiene todas las categorías activas de una empresa.
     *
     * @param enterpriseId ID de la empresa
     * @param page Paginación con ordenamiento
     * @return Página de categorías activas
     */
    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND c.state = true")
    Page<CategoryEntity> getActiveCategoriesBy(@Param("enterpriseId") String enterpriseId, Pageable page);

    /**
     * Cuenta el total de categorías activas por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad total de categorías activas
     */
    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND c.state = true")
    long countActiveByEnterpriseId(@Param("enterpriseId") String enterpriseId);

}
