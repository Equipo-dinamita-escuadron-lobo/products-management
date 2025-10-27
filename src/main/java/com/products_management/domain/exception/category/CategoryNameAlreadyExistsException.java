package com.products_management.domain.exception.category;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando se intenta crear o actualizar una categoría con un nombre que ya existe.
 */
public class CategoryNameAlreadyExistsException extends BaseBusinessException {
    
    public CategoryNameAlreadyExistsException() {
        super(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS);
    }
    
    public CategoryNameAlreadyExistsException(String name) {
        super(ErrorCode.CATEGORY_NAME_ALREADY_EXISTS, 
              String.format("Ya existe una categoría con el nombre '%s'", name));
    }
}
