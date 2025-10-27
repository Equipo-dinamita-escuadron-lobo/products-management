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

   
    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND c.state = :state")
    Page<CategoryEntity> getCategoriesByEnterpriseIdAndState(@Param("enterpriseId") String enterpriseId, @Param("state") Boolean state, Pageable page);

    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<CategoryEntity> findByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId, @Param("search") String search, Pageable page);

  
    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND " +
           "(LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    long countByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId, @Param("search") String search);

    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId")
    long countByEnterpriseId(@Param("enterpriseId") String enterpriseId);


    @Query("SELECT c FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND c.state = true")
    Page<CategoryEntity> getActiveCategoriesBy(@Param("enterpriseId") String enterpriseId, Pageable page);

    @Query("SELECT COUNT(c) FROM CategoryEntity c WHERE c.enterpriseId = :enterpriseId AND c.state = true")
    long countActiveByEnterpriseId(@Param("enterpriseId") String enterpriseId);

}
