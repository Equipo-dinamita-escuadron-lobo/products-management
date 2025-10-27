package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

import java.util.Optional;

/**
 * Interfaz para el repositorio de unidades de medida que extiende JpaRepository.
 * Proporciona métodos para realizar operaciones CRUD en UnitOfMeasureEntity.
 */
public interface IUnitOfMeasureRepository extends JpaRepository<UnitOfMeasureEntity, Long> {
    
    /**
     * Busca una unidad de medida por ID e ID de empresa.
     */
    Optional<UnitOfMeasureEntity> findByIdAndEnterpriseId(Long id, String enterpriseId);
    
    /**
     * Busca unidades de medida por ID de empresa.
     */
    List<UnitOfMeasureEntity> findByEnterpriseId(String enterpriseId);
    
    /**
     * Busca unidades de medida activas por ID de empresa.
     */
    List<UnitOfMeasureEntity> findByEnterpriseIdAndState(String enterpriseId, boolean state);
    
    /**
     * Verifica si existe una unidad de medida con el nombre especificado para una empresa.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe una unidad de medida con la abreviación especificada para una empresa.
     */
    boolean existsByAbbreviationAndEnterpriseId(String abbreviation, String enterpriseId);
    
    /**
     * Verifica si existe una unidad de medida con el nombre especificado para una empresa, excluyendo un ID específico.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);
    
    /**
     * Verifica si existe una unidad de medida con la abreviación especificada para una empresa, excluyendo un ID específico.
     */
    boolean existsByAbbreviationAndEnterpriseIdAndIdNot(String abbreviation, String enterpriseId, Long id);

    @Query("SELECT u FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :enterpriseId")
    Page<UnitOfMeasureEntity> getUnitOfMeasuresBy(String enterpriseId, Pageable page);

    /**
     * Obtiene todas las unidades de medida de una empresa filtradas por estado.
     *
     * @param enterpriseId ID de la empresa
     * @param state Estado de las unidades de medida (true=activas, false=inactivas)
     * @param page Paginación
     * @return Página de unidades de medida filtradas por estado
     */
    @Query("SELECT u FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :enterpriseId AND u.state = :state")
    Page<UnitOfMeasureEntity> getUnitOfMeasuresByEnterpriseIdAndState(@Param("enterpriseId") String enterpriseId, @Param("state") Boolean state, Pageable page);

    /**
     * Busca unidades de medida por empresa y término de búsqueda.
     * Busca en: nombres, abreviaturas.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @param page Paginación con ordenamiento
     * @return Página de unidades de medida que coinciden con la búsqueda
     */
    @Query("SELECT u FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :enterpriseId AND " +
           "(LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.abbreviation) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<UnitOfMeasureEntity> findByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId, @Param("search") String search, Pageable page);

    /**
     * Cuenta unidades de medida por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de unidades de medida que coinciden
     */
    @Query("SELECT COUNT(u) FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :enterpriseId AND " +
           "(LOWER(u.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
           "LOWER(u.abbreviation) LIKE LOWER(CONCAT('%', :search, '%')))")
    long countByEnterpriseIdAndSearch(@Param("enterpriseId") String enterpriseId, @Param("search") String search);

    @Query("SELECT COUNT(u) FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :enterpriseId")
    long countByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * Obtiene todas las unidades de medida activas de una empresa.
     *
     * @param enterpriseId ID de la empresa
     * @param page Paginación con ordenamiento
     * @return Página de unidades de medida activas
     */
    @Query("SELECT u FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :enterpriseId AND u.state = true")
    Page<UnitOfMeasureEntity> getActiveUnitOfMeasuresBy(@Param("enterpriseId") String enterpriseId, Pageable page);

    /**
     * Cuenta el total de unidades de medida activas por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad total de unidades de medida activas
     */
    @Query("SELECT COUNT(u) FROM UnitOfMeasureEntity u WHERE u.enterpriseId = :enterpriseId AND u.state = true")
    long countActiveByEnterpriseId(@Param("enterpriseId") String enterpriseId);

}
