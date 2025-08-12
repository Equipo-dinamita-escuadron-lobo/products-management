package com.products_management.domain.exception.unitOfMeasure;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando se intenta crear o actualizar una unidad de medida con un nombre que ya existe.
 */
public class UnitOfMeasureNameAlreadyExistsException extends BaseBusinessException {
    
    public UnitOfMeasureNameAlreadyExistsException() {
        super(ErrorCode.UNITOFMEASURE_NAME_ALREADY_EXISTS);
    }
    
    public UnitOfMeasureNameAlreadyExistsException(String name) {
        super(ErrorCode.UNITOFMEASURE_NAME_ALREADY_EXISTS, 
              String.format("Ya existe una unidad de medida con el nombre: '%s'", name));
    }
}
