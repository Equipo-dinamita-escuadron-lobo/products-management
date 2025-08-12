package com.products_management.domain.exception.unitOfMeasure;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando no se encuentra una unidad de medida.
 */
public class UnitOfMeasureNotFoundException extends BaseBusinessException {
    
    public UnitOfMeasureNotFoundException() {
        super(ErrorCode.UNITOFMEASURE_NOT_FOUND);
    }
    
    public UnitOfMeasureNotFoundException(Long id) {
        super(ErrorCode.UNITOFMEASURE_NOT_FOUND, 
              String.format("No se encontró la unidad de medida con ID: %d", id));
    }
    
    public UnitOfMeasureNotFoundException(String name) {
        super(ErrorCode.UNITOFMEASURE_NOT_FOUND, 
              String.format("No se encontró la unidad de medida con nombre: %s", name));
    }
}
