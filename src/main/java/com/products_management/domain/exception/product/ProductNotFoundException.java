package com.products_management.domain.exception.product;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando no se encuentra un producto.
 */
public class ProductNotFoundException extends BaseBusinessException {
    
    public ProductNotFoundException() {
        super(ErrorCode.PRODUCT_NOT_FOUND);
    }
    
    public ProductNotFoundException(Long id) {
        super(ErrorCode.PRODUCT_NOT_FOUND, 
              String.format("No se encontró el producto con ID %d", id));
    }
    
    public ProductNotFoundException(String code) {
        super(ErrorCode.PRODUCT_NOT_FOUND, 
              String.format("No se encontró el producto con código %s", code));
    }
}
