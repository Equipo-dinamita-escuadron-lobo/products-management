package com.products_management.domain.exception.unitOfMeasure;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando se intenta crear o actualizar una unidad de medida con una abreviación que ya existe.
 */
public class UnitOfMeasureAbbreviationAlreadyExistsException extends BaseBusinessException {
    
    public UnitOfMeasureAbbreviationAlreadyExistsException() {
        super(ErrorCode.UNITOFMEASURE_ABBREVIATION_ALREADY_EXISTS);
    }
    
    public UnitOfMeasureAbbreviationAlreadyExistsException(String abbreviation) {
        super(ErrorCode.UNITOFMEASURE_ABBREVIATION_ALREADY_EXISTS, 
              String.format("Ya existe una unidad de medida con la abreviación '%s'", abbreviation));
    }
}
