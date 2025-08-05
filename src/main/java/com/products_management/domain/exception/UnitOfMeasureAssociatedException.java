package com.products_management.domain.exception;

/**
 * Excepción lanzada cuando se intenta eliminar una unidad de medida que está asociada con productos u otros elementos.
 */
public class UnitOfMeasureAssociatedException extends BaseBusinessException {
    
    public UnitOfMeasureAssociatedException() {
        super(ErrorCode.UNITOFMEASURE_ASSOCIATED);
    }
    
    public UnitOfMeasureAssociatedException(Long id) {
        super(ErrorCode.UNITOFMEASURE_ASSOCIATED, 
              String.format("La unidad de medida con ID %d no se puede eliminar porque está asociada con productos", id));
    }
    
    public UnitOfMeasureAssociatedException(String name) {
        super(ErrorCode.UNITOFMEASURE_ASSOCIATED, 
              String.format("La unidad de medida '%s' no se puede eliminar porque está asociada con productos", name));
    }
}