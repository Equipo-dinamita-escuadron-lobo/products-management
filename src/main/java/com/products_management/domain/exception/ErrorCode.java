package com.products_management.domain.exception;

import lombok.Getter;

/**
 * Catálogo de errores utilizado para identificar y describir errores comunes.
 */
@Getter
public enum ErrorCode {

    GENERIC_ERROR("GENERIC_ERROR", "Ha ocurrido un error"),

    //Codigos de producto
    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Producto no encontrado"),
    INVALID_PRODUCT("INVALID_PRODUCT", "Producto inválido"),

    //Codigos de unidad de medida
    UNITOFMEASURE_NOT_FOUND("UNITOFMEASURE_NOT_FOUND", "Unidad de medida no encontrada"),
    INVALID_UNITOFMEASURE("INVALID_UNITOFMEASURE", "Unidad de medida inválida"),
    UNITOFMEASURE_ASSOCIATED("UNITOFMEASURE_ASSOCIATED_WITH_PRODUCT", "La unidad de medida está asociada con un producto"),
    UNITOFMEASURE_NAME_ALREADY_EXISTS("UNITOFMEASURE_NAME_ALREADY_EXISTS", "Ya existe una unidad de medida con este nombre"),
    UNITOFMEASURE_ABBREVIATION_ALREADY_EXISTS("UNITOFMEASURE_ABBREVIATION_ALREADY_EXISTS", "Ya existe una unidad de medida con esta abreviación"),

    //Codigos de categoría
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", "Categoría no encontrada"),
    INVALID_CATEGORY("INVALID_CATEGORY", "Categoría inválida"),
    CATEGORY_ASSOCIATED("CATEGORY_ASSOCIATED_WITH_PRODUCT", "La categoría está asociada con un producto"),
    CATEGORY_NAME_ALREADY_EXISTS("CATEGORY_NAME_ALREADY_EXISTS", "Ya existe una categoría con este nombre"),

    //Codigos de tipo de producto
    PRODUCT_TYPE_NOT_FOUND("PRODUCT_TYPE_NOT_FOUND", "Tipo de producto no encontrado"),
    INVALID_PRODUCT_TYPE("INVALID_PRODUCT_TYPE", "Tipo de producto inválido"),
    PRODUCT_TYPE_ASSOCIATED("PRODUCT_TYPE_ASSOCIATED_WITH_PRODUCT", "El tipo de producto está asociado con un producto"),
    PRODUCT_TYPE_NAME_ALREADY_EXISTS("PRODUCT_TYPE_NAME_ALREADY_EXISTS", "Ya existe un tipo de producto con este nombre");

    private final String code;
    private final String message;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
