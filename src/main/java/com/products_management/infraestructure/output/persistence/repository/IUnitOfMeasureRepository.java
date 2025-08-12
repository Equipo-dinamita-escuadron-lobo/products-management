package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.UnitOfMeasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Interfaz para el repositorio de unidades de medida que extiende JpaRepository.
 * Proporciona métodos para realizar operaciones CRUD en UnitOfMeasureEntity.
 */
public interface IUnitOfMeasureRepository extends JpaRepository<UnitOfMeasureEntity, Long> {
    
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
    
}
