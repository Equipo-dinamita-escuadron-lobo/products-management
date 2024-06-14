package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz para el repositorio de categorías que extiende JpaRepository.
 * Proporciona métodos para realizar operaciones CRUD en CategoryEntity.
 */
public interface ICategoryRepository extends JpaRepository<CategoryEntity, Long> {
}
