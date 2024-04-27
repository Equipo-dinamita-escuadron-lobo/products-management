package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;

import java.util.List;

public interface ICategoryRestMapper {
    Category toCategory(CategoryCreateRequest categoryCreateRequest);
    CategoryResponse toCategoryResponse(Category category);
    List<CategoryResponse> toCategoryResponseList(List<Category> categoryList);
}
