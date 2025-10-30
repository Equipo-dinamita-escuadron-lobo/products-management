package com.products_management.domain.exception.product;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando se intenta crear o actualizar un producto con una referencia que ya existe.
 */
public class ProductReferenceAlreadyExistsException extends BaseBusinessException {
    
    public ProductReferenceAlreadyExistsException() {
        super(ErrorCode.PRODUCT_REFERENCE_ALREADY_EXISTS);
    }
    
    public ProductReferenceAlreadyExistsException(String reference) {
        super(ErrorCode.PRODUCT_REFERENCE_ALREADY_EXISTS, 
              String.format("Ya existe un producto con la referencia '%s'", reference));
    }
}
