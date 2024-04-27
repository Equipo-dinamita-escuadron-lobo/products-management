package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;

import java.util.List;

public interface ICategoryPersistenceMapper {

    CategoryEntity toCategoryEntity(Category category);
    Category toCategory(CategoryEntity categoryEntity);
    List<Category> toCategoryList(List<CategoryEntity> categoryEntityList);
}
