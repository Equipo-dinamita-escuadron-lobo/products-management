package com.products_management.domain.exception.product;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * @brief Excepción para producto no encontrado
 *
 * Se lanza cuando se intenta acceder a un producto que no existe en el sistema.
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
