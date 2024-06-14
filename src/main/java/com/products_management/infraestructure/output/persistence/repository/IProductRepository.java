package com.products_management.infraestructure.output.persistence.repository;

import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Interfaz para el repositorio de productos que extiende JpaRepository.
 * Proporciona métodos para realizar operaciones CRUD en ProductEntity.
 */
public interface IProductRepository extends JpaRepository<ProductEntity, Long> {

}
