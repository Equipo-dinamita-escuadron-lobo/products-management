package com.products_management.infraestructure.input.rest.mapper.impl;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.input.rest.mapper.interfaces.ICategoryRestMapper;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación de {@link ICategoryRestMapper} que realiza la conversión entre entidades de categoría y sus representaciones REST.
 */
@Component
public class CategoryRestMapperImpl implements ICategoryRestMapper {

    /**
     * Convierte una solicitud de creación de categoría ({@link CategoryCreateRequest}) en una entidad de categoría ({@link Category}).
     *
     * @param categoryCreateRequest la solicitud de creación de categoría.
     * @return la entidad de categoría convertida.
     */
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
        categoryBuilder.taxId(categoryCreateRequest.getTaxId());
        categoryBuilder.state(categoryCreateRequest.getState());

        return categoryBuilder.build();
    }

    /**
     * Convierte una entidad de categoría ({@link Category}) en una respuesta de categoría ({@link CategoryResponse}).
     *
     * @param category la entidad de categoría.
     * @return la respuesta de categoría convertida.
     */
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
        categoryResponseBuilder.costId(category.getCostId());
        categoryResponseBuilder.saleId(category.getSaleId());
        categoryResponseBuilder.returnId(category.getReturnId());
        categoryResponseBuilder.state(category.getState());

        return categoryResponseBuilder.build();
    }

    /**
     * Convierte una lista de entidades de categoría ({@link Category}) en una lista de respuestas de categoría ({@link CategoryResponse}).
     *
     * @param categoryList la lista de entidades de categoría.
     * @return la lista de respuestas de categoría convertida.
     */
    @Override
    public List<CategoryResponse> toCategoryResponseList(List<Category> categoryList) {
        if(categoryList == null) {
            return null;
        }
        List<CategoryResponse> categoryResponseList = new ArrayList<>(categoryList.size());
        for(Category category : categoryList) {
            categoryResponseList.add(toCategoryResponse(category));
        }
        return categoryResponseList;
    }
}
