package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

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
    
}
