package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;

import java.util.List;

/**
 * Interfaz que define métodos para mapear entre entidades de categoría y sus representaciones REST.
 */
public interface ICategoryRestMapper {

    /**
     * Convierte una solicitud de creación de categoría ({@link CategoryCreateRequest}) en una entidad de categoría ({@link Category}).
     *
     * @param categoryCreateRequest la solicitud de creación de categoría.
     * @return la entidad de categoría convertida.
     */
    Category toCategory(CategoryCreateRequest categoryCreateRequest);

    /**
     * Convierte una entidad de categoría ({@link Category}) en una respuesta de categoría ({@link CategoryResponse}).
     *
     * @param category la entidad de categoría.
     * @return la respuesta de categoría convertida.
     */
    CategoryResponse toCategoryResponse(Category category);

    /**
     * Convierte una lista de entidades de categoría ({@link Category}) en una lista de respuestas de categoría ({@link CategoryResponse}).
     *
     * @param categoryList la lista de entidades de categoría.
     * @return la lista de respuestas de categoría convertida.
     */
    List<CategoryResponse> toCategoryResponseList(List<Category> categoryList);
}
