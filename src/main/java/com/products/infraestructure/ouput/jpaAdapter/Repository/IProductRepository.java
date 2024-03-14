package com.products.infraestructure.ouput.jpaAdapter.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.products.infraestructure.ouput.jpaAdapter.Entity.ProductEntity;

public interface IProductRepository extends JpaRepository<ProductEntity, String>{
    
}
