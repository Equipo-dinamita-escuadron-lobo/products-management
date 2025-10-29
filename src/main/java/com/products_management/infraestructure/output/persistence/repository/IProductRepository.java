package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.ProductEntity;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Interfaz para el repositorio de productos que extiende JpaRepository.
 * Proporciona métodos para realizar operaciones CRUD en ProductEntity.
 */
public interface IProductRepository extends JpaRepository<ProductEntity, Long> {
    
    // Search for products from a company modified AFTER the date provided.
    List<ProductEntity> findByEnterpriseIdAndLastModifiedDateAfter(String enterpriseId, Instant lastSyncDate);
    
    /**
     * Busca productos por ID de empresa.
     */
    List<ProductEntity> findByEnterpriseId(String enterpriseId);
    
    /**
     * Busca un producto por ID y empresa.
     */
    Optional<ProductEntity> findByIdAndEnterpriseId(Long id, String enterpriseId);
    
    /**
     * Busca productos activos por ID de empresa.
     */
    List<ProductEntity> findByEnterpriseIdAndState(String enterpriseId, boolean state);
    
    /**
     * Busca productos por ID de categoría.
     */
    List<ProductEntity> findByCategoryId(Long categoryId);
    
    /**
     * Busca productos por ID de unidad de medida.
     */
    List<ProductEntity> findByUnitOfMeasureId(Long unitOfMeasureId);
    
    /**
     * Busca productos por ID de tipo de producto.
     */
    List<ProductEntity> findByProductTypeId(Long productTypeId);
    
    /**
     * Verifica si existe un producto con el nombre especificado para una empresa.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe un producto con la referencia especificada para una empresa.
     */
    boolean existsByReferenceAndEnterpriseId(String reference, String enterpriseId);
    
    /**
     * Verifica si existe un producto con el nombre especificado para una empresa, excluyendo un ID específico.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);
    
    /**
     * Verifica si existe un producto con la referencia especificada para una empresa, excluyendo un ID específico.
     */
    boolean existsByReferenceAndEnterpriseIdAndIdNot(String reference, String enterpriseId, Long id);
    
    /**
     * Busca productos por ID de empresa con filtros de búsqueda y paginación.
     */
    @Query("SELECT p FROM ProductEntity p WHERE p.enterpriseId = :enterpriseId " +
           "AND (:search IS NULL OR :search = '' OR " +
           "LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.reference) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<ProductEntity> findByEnterpriseIdWithFilters(String enterpriseId, String search, Pageable pageable);
    
    /**
     * Cuenta productos por ID de empresa con filtros de búsqueda.
     */
    @Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.enterpriseId = :enterpriseId " +
           "AND (:search IS NULL OR :search = '' OR " +
           "LOWER(p.code) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(p.reference) LIKE LOWER(CONCAT('%', :search, '%')))")
    long countByEnterpriseIdWithFilters(String enterpriseId, String search);
    
    /**
     * Cuenta todos los productos por ID de empresa.
     */
    long countByEnterpriseId(String enterpriseId);
    
}
