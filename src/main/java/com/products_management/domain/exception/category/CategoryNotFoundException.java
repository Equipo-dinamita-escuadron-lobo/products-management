package com.products_management.domain.exception.category;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando no se encuentra una categoría.
 */
public class CategoryNotFoundException extends BaseBusinessException {
    
    public CategoryNotFoundException() {
        super(ErrorCode.CATEGORY_NOT_FOUND);
    }
    
    public CategoryNotFoundException(Long id) {
        super(ErrorCode.CATEGORY_NOT_FOUND, 
              String.format("No se encontró la categoría con ID %d", id));
    }
    
    public CategoryNotFoundException(String nameOrMessage, boolean isCustomMessage) {
        super(ErrorCode.CATEGORY_NOT_FOUND, 
              isCustomMessage ? nameOrMessage : 
              String.format("No se encontró la categoría con nombre %s", nameOrMessage));
    }
}
