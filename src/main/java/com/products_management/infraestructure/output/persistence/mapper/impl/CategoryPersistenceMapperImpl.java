package com.products_management.infraestructure.output.persistence.mapper.impl;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.ICategoryPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del mapper de persistencia para la entidad Category.
 */
@Component
public class CategoryPersistenceMapperImpl implements ICategoryPersistenceMapper {

    /**
     * Convierte un objeto Category del dominio en una CategoryEntity de persistencia.
     * @param category Objeto Category del dominio.
     * @return CategoryEntity correspondiente.
     */
    @Override
    public CategoryEntity toCategoryEntity(Category category) {
        if (category == null) {
            return null;
        }

        CategoryEntity categoryEntity = new CategoryEntity();
        categoryEntity.setId(category.getId());
        categoryEntity.setName(category.getName());
        categoryEntity.setDescription(category.getDescription());
        categoryEntity.setEnterpriseId(category.getEnterpriseId());
        categoryEntity.setInventoryId(category.getInventoryId());
        categoryEntity.setCostId(category.getCostId());
        categoryEntity.setSaleId(category.getSaleId());
        categoryEntity.setReturnId(category.getReturnId());
        categoryEntity.setState(category.getState() == null ? "true" : category.getState());

        return categoryEntity;
    }

    /**
     * Convierte una CategoryEntity de persistencia en un objeto Category del dominio.
     * @param categoryEntity CategoryEntity de persistencia.
     * @return Objeto Category correspondiente.
     */
    @Override
    public Category toCategory(CategoryEntity categoryEntity) {
        if (categoryEntity == null) {
            return null;
        }

        return Category.builder()
                .id(categoryEntity.getId())
                .name(categoryEntity.getName())
                .description(categoryEntity.getDescription())
                .enterpriseId(categoryEntity.getEnterpriseId())
                .inventoryId(categoryEntity.getInventoryId())
                .costId(categoryEntity.getCostId())
                .saleId(categoryEntity.getSaleId())
                .returnId(categoryEntity.getReturnId())
                .state(categoryEntity.getState())
                .build();
    }

    /**
     * Convierte una lista de CategoryEntity de persistencia en una lista de objetos Category del dominio.
     * @param categoryEntityList Lista de CategoryEntity de persistencia.
     * @return Lista de objetos Category correspondiente.
     */
    @Override
    public List<Category> toCategoryList(List<CategoryEntity> categoryEntityList) {
        if (categoryEntityList == null) {
            return null;
        }

        List<Category> categoryList = new ArrayList<>(categoryEntityList.size());
        for (CategoryEntity categoryEntity : categoryEntityList) {
            categoryList.add(toCategory(categoryEntity));
        }
        return categoryList;
    }
}
