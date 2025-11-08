package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.domain.model.Category;
import com.products_management.infraestructure.input.rest.dto.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.dto.response.CategoryResponse;

import java.util.List;

import org.mapstruct.Mapper;

/**
 * @brief Mapper para transformación de entidades de categoría
 *
 * Define contratos de mapeo entre entidades de dominio Category
 * y DTOs de request/response para operaciones REST.
 */
@Mapper(componentModel = "spring")
public interface ICategoryRestMapper {

    /**
     * @brief Convierte DTO de creación a entidad de categoría
     * @param categoryCreateRequest solicitud de creación de categoría
     * @return entidad de categoría convertida
     */
    Category toCategory(CategoryCreateRequest categoryCreateRequest);

    /**
     * @brief Convierte entidad de categoría a DTO de respuesta
     * @param category entidad de categoría
     * @return respuesta de categoría convertida
     */
    CategoryResponse toCategoryResponse(Category category);

    /**
     * @brief Convierte lista de entidades a lista de DTOs de respuesta
     * @param categoryList lista de entidades de categoría
     * @return lista de respuestas de categoría convertidas
     */
    List<CategoryResponse> toCategoryResponseList(List<Category> categoryList);
}
