package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IProductTypeRepository extends JpaRepository<ProductTypeEntity, Long> {
    /**
     * Busca un tipo de producto por ID e ID de empresa.
     */
    Optional<ProductTypeEntity> findByIdAndEnterpriseId(Long id, String enterpriseId);

    /**
     * Verifica si existe un tipo de producto con el nombre especificado para una
     * empresa.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);

    /**
     * Verifica si existe un tipo de producto con el nombre especificado para una
     * empresa, excluyendo un ID específico.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);

    @Query("SELECT p FROM ProductTypeEntity p WHERE p.enterpriseId = :enterpriseId")
    Page<ProductTypeEntity> getProductTypesBy(String enterpriseId, Pageable page);

    /**
     * Busca tipos de producto por empresa y término de búsqueda.
     * Busca en: nombres, descripciones.
     *
     * @param enterpriseId ID de la empresa
     * @param search       Término de búsqueda
     * @param page         Paginación con ordenamiento
     * @return Página de tipos de producto que coinciden con la búsqueda
     */
    @Query("SELECT p FROM ProductTypeEntity p WHERE p.enterpriseId = :enterpriseId AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ProductTypeEntity> findByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId,
            @Param("search") String search, Pageable page);

    /**
     * Cuenta tipos de producto por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search       Término de búsqueda
     * @return Cantidad de tipos de producto que coinciden
     */
    @Query("SELECT COUNT(p) FROM ProductTypeEntity p WHERE p.enterpriseId = :enterpriseId AND " +
            "(LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    long countByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId, @Param("search") String search);

    @Query("SELECT COUNT(p) FROM ProductTypeEntity p WHERE p.enterpriseId = :enterpriseId")
    long countByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * Busca tipos de producto activados por empresa con paginación.
     *
     * @param enterpriseId ID de la empresa
     * @param pageable Paginación
     * @return Página de tipos de producto activados
     */
    @Query("SELECT p FROM ProductTypeEntity p WHERE p.enterpriseId = :enterpriseId AND p.state = true")
    Page<ProductTypeEntity> findActivatedByEnterpriseId(@Param("enterpriseId") String enterpriseId, Pageable pageable);

    /**
     * Cuenta tipos de producto activados por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad de tipos de producto activados
     */
    @Query("SELECT COUNT(p) FROM ProductTypeEntity p WHERE p.enterpriseId = :enterpriseId AND p.state = true")
    long countActivatedByEnterpriseId(@Param("enterpriseId") String enterpriseId);
}
