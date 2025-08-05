package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;

import java.util.List;

/**
 * Interfaz para mapear entre entidades de persistencia (ProductTypeEntity) y objetos del dominio (ProductType).
 */
public interface IProductTypePersistenceMapper {

    /**
     * Convierte un objeto ProductType del dominio en un ProductTypeEntity de persistencia.
     * @param productType Objeto ProductType del dominio.
     * @return ProductTypeEntity correspondiente.
     */
    ProductTypeEntity toProductTypeEntity(ProductType productType);

    /**
     * Convierte un ProductTypeEntity de persistencia en un objeto ProductType del dominio.
     * @param productTypeEntity ProductTypeEntity de persistencia.
     * @return Objeto ProductType correspondiente.
     */
    ProductType toProductType(ProductTypeEntity productTypeEntity);

    /**
     * Convierte una lista de ProductTypeEntity de persistencia en una lista de objetos ProductType del dominio.
     * @param productTypeEntityList Lista de ProductTypeEntity de persistencia.
     * @return Lista de objetos ProductType correspondiente.
     */
    List<ProductType> toProductTypeList(List<ProductTypeEntity> productTypeEntityList);
}
