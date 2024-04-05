package com.products_management.utils;

import lombok.Getter;

@Getter
public enum ErrorCatalog {

    PRODUCT_NOT_FOUND("PRODUCT_NOT_FOUND", "Product not found"),
    INVALID_PRODUCT("INVALID_PRODUCT", "Invalid product"),
    GENERIC_ERROR("GENERIC_ERROR", "An error occurred");

    private final String code;
    private final String message;

    ErrorCatalog(String code, String message) {
        this.code = code;
        this.message = message;
    }
}
