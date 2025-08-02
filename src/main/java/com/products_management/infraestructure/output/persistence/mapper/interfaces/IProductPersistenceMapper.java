package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz para mapear entre entidades de persistencia (ProductEntity) y objetos del dominio (Product).
 */
@Mapper(componentModel = "spring")
public interface IProductPersistenceMapper {

    /**
     * Convierte un objeto Product del dominio en una ProductEntity de persistencia.
     * @param product Objeto Product del dominio.
     * @return ProductEntity correspondiente.
     */
    @Mapping(target = "tenantId", ignore = true)
    @Mapping(target = "lastModifiedDate", ignore = true)
    ProductEntity toProductEntity(Product product);

    /**
     * Convierte una ProductEntity de persistencia en un objeto Product del dominio.
     * @param productEntity ProductEntity de persistencia.
     * @return Objeto Product correspondiente.
     */
    Product toProduct(ProductEntity productEntity);

    /**
     * Convierte una lista de ProductEntity de persistencia en una lista de objetos Product del dominio.
     * @param productList Lista de ProductEntity de persistencia.
     * @return Lista de objetos Product correspondiente.
     */
    List<Product> toProductList(List<ProductEntity> productList);
}
