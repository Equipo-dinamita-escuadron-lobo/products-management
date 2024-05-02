package com.products_management.utils;

import lombok.Getter;

@Getter
public enum ErrorCatalog {

    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found"),
    INVALID_PRODUCT("INVALID_PRODUCT", "Invalid product"),
    GENERIC_ERROR("GENERIC_ERROR", "An error occurred"),
    UNITOFMEASURE_NOT_FOUND("UNITOFMEASURE_NOT_FOUND", "Unit of measure not found"),
    INVALID_UNITOFMEASURE("INVALID_UNITOFMEASURE", "Invalid unit of measure"),
    CATEGORY_NOT_FOUND("CATEGORY_NOT_FOUND", "Category not found"),
    INVALID_CATEGORY("INVALID_CATEGORY", "Invalid category"),
  UNITOFMEASURE_ASSOCIATED("UNITOFMEASURE_ASSOCIATED_WITH_PRODUCT", "Unit of measure is associated with a product"),
    CATEGORY_ASSOCIATED("CATEGORY_ASSOCIATED_WITH_PRODUCT", "Category is associated with a product");


    private final String code;
    private final String message;

    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
