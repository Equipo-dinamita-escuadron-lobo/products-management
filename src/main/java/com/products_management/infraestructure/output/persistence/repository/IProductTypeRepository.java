package com.products_management.infraestructure.output.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface IProductTypeRepository extends JpaRepository<ProductTypeEntity, Long> {
    /**
     * Busca un tipo de producto por ID e ID de empresa.
     */
    Optional<ProductTypeEntity> findByIdAndEnterpriseId(Long id, String enterpriseId);
    
    List<ProductTypeEntity> findByEnterpriseId(String enterpriseId);
    
    /**
     * Busca tipos de producto activos por ID de empresa.
     */
    List<ProductTypeEntity> findByEnterpriseIdAndState(String enterpriseId, boolean state);
    
    /**
     * Verifica si existe un tipo de producto con el nombre especificado para una empresa.
     */
    boolean existsByNameAndEnterpriseId(String name, String enterpriseId);
    
    /**
     * Verifica si existe un tipo de producto con el nombre especificado para una empresa, excluyendo un ID específico.
     */
    boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id);
}
