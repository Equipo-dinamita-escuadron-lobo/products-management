package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz para mapear entre entidades de persistencia (CategoryEntity) y objetos del dominio (Category).
 */
@Mapper(componentModel = "spring")
public interface ICategoryPersistenceMapper {

    /**
     * Convierte un objeto Category del dominio en una CategoryEntity de persistencia.
     * @param category Objeto Category del dominio.
     * @return CategoryEntity correspondiente.
     */
    @Mapping(target = "tenantId", ignore = true)
    CategoryEntity toCategoryEntity(Category category);

    /**
     * Convierte una CategoryEntity de persistencia en un objeto Category del dominio.
     * @param categoryEntity CategoryEntity de persistencia.
     * @return Objeto Category correspondiente.
     */
    Category toCategory(CategoryEntity categoryEntity);

    /**
     * Convierte una lista de CategoryEntity de persistencia en una lista de objetos Category del dominio.
     * @param categoryEntityList Lista de CategoryEntity de persistencia.
     * @return Lista de objetos Category correspondiente.
     */
    List<Category> toCategoryList(List<CategoryEntity> categoryEntityList);
}
