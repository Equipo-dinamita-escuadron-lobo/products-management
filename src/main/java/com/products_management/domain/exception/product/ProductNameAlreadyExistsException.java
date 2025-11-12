package com.products_management.domain.exception.product;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * @brief Excepción para nombre de producto duplicado
 *
 * Se lanza cuando se intenta crear o actualizar un producto con un nombre
 * que ya está siendo usado por otro producto en la misma empresa.
 */
public class ProductNameAlreadyExistsException extends BaseBusinessException {
    
    public ProductNameAlreadyExistsException() {
        super(ErrorCode.PRODUCT_NAME_ALREADY_EXISTS);
    }
    
    public ProductNameAlreadyExistsException(String name) {
        super(ErrorCode.PRODUCT_NAME_ALREADY_EXISTS, 
              String.format("Ya existe un producto con el nombre '%s'", name));
    }
}
