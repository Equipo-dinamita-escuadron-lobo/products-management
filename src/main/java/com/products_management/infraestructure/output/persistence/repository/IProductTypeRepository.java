package com.products_management.infraestructure.output.persistence.repository;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;

import java.util.List;

@Repository
public interface IProductTypeRepository extends JpaRepository<ProductTypeEntity, Long> {
    List<ProductTypeEntity> findByEnterpriseId(Long enterpriseId);
}
