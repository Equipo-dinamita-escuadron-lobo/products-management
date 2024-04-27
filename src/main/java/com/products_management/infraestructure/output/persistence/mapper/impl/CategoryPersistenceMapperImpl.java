package com.products_management.infraestructure.output.persistence.mapper.impl;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.ICategoryPersistenceMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryPersistenceMapperImpl implements ICategoryPersistenceMapper {
    @Override
    public CategoryEntity toCategoryEntity(Category category) {
        if(category == null) {
            return null;
        }
        CategoryEntity categoryEntityBuilder = new CategoryEntity();

        categoryEntityBuilder.setId(category.getId());
        categoryEntityBuilder.setName(category.getName());
        categoryEntityBuilder.setDescription(category.getDescription());
        categoryEntityBuilder.setEnterpriseId(category.getEnterpriseId());
        categoryEntityBuilder.setInventoryId(category.getInventoryId());
        categoryEntityBuilder.setCostId(category.getCostId());
        categoryEntityBuilder.setSaleId(category.getSaleId());
        categoryEntityBuilder.setReturnId(category.getReturnId());

        return categoryEntityBuilder;

    }

    @Override
    public Category toCategory(CategoryEntity categoryEntity) {
        if(categoryEntity == null) {
            return null;
        }

        Category.CategoryBuilder categoryBuilder = Category.builder();

        categoryBuilder.id(categoryEntity.getId());
        categoryBuilder.name(categoryEntity.getName());
        categoryBuilder.description(categoryEntity.getDescription());
        categoryBuilder.enterpriseId(categoryEntity.getEnterpriseId());
        categoryBuilder.inventoryId(categoryEntity.getInventoryId());
        categoryBuilder.costId(categoryEntity.getCostId());
        categoryBuilder.saleId(categoryEntity.getSaleId());
        categoryBuilder.returnId(categoryEntity.getReturnId());

        return categoryBuilder.build();
    }

    @Override
    public List<Category> toCategoryList(List<CategoryEntity> categoryEntityList) {
        if(categoryEntityList == null) {
            return null;
        }
        List<Category> categoryList = new ArrayList<Category>(categoryEntityList.size());
        for(CategoryEntity categoryEntity : categoryEntityList) {
            categoryList.add(toCategory(categoryEntity));
        }
        return categoryList;
    }
}
