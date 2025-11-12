package com.products_management.domain.exception.category;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * @brief Excepción para nombre de categoría duplicado
 *
 * Se lanza cuando se intenta crear o actualizar una categoría con un nombre
 * que ya está siendo usado por otra categoría en la misma empresa.
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
