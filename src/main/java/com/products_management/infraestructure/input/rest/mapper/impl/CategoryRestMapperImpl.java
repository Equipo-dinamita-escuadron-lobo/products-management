package com.products_management.infraestructure.input.rest.mapper.impl;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.input.rest.mapper.interfaces.ICategoryRestMapper;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class CategoryRestMapperImpl implements ICategoryRestMapper {
    @Override
    public Category toCategory(CategoryCreateRequest categoryCreateRequest) {
        if(categoryCreateRequest == null) {
            return null;
        }
        Category.CategoryBuilder categoryBuilder = Category.builder();
        categoryBuilder.id(categoryCreateRequest.getId());
        categoryBuilder.name(categoryCreateRequest.getName());
        categoryBuilder.description(categoryCreateRequest.getDescription());
        categoryBuilder.enterpriseId(categoryCreateRequest.getEnterpriseId());
        categoryBuilder.inventoryId(categoryCreateRequest.getInventoryId());
        categoryBuilder.costId(categoryCreateRequest.getCostId());
        categoryBuilder.saleId(categoryCreateRequest.getSaleId());
        categoryBuilder.returnId(categoryCreateRequest.getReturnId());
        categoryBuilder.state(categoryCreateRequest.getState());

        return categoryBuilder.build();
    }

    @Override
    public CategoryResponse toCategoryResponse(Category category) {
        if(category == null) {
            return null;
        }
        CategoryResponse.CategoryResponseBuilder categoryResponseBuilder = CategoryResponse.builder();
        categoryResponseBuilder.id(category.getId());
        categoryResponseBuilder.name(category.getName());
        categoryResponseBuilder.description(category.getDescription());
        categoryResponseBuilder.enterpriseId(category.getEnterpriseId());
        categoryResponseBuilder.inventoryId(category.getInventoryId());

        return categoryResponseBuilder.build();
    }

    @Override
    public List<CategoryResponse> toCategoryResponseList(List<Category> categoryList) {
        if(categoryList == null) {
            return null;
        }
        List<CategoryResponse> categoryResponseList = new ArrayList<CategoryResponse>(categoryList.size());
        for(Category category : categoryList) {
            categoryResponseList.add(toCategoryResponse(category));
        }
        return categoryResponseList;
    }
}
