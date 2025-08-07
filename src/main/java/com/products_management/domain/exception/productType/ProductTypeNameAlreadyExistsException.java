package com.products_management.domain.exception.productType;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando se intenta crear o actualizar un tipo de producto con un nombre que ya existe.
 */
public class ProductTypeNameAlreadyExistsException extends BaseBusinessException {
    
    public ProductTypeNameAlreadyExistsException() {
        super(ErrorCode.PRODUCT_TYPE_NAME_ALREADY_EXISTS);
    }
    
    public ProductTypeNameAlreadyExistsException(String name) {
        super(ErrorCode.PRODUCT_TYPE_NAME_ALREADY_EXISTS, 
              String.format("Ya existe un tipo de producto con el nombre: '%s'", name));
    }
}
