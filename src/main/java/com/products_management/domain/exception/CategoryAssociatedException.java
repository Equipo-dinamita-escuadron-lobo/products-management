package com.products_management.domain.exception;

/**
 * Excepción lanzada cuando se intenta eliminar una categoría que está asociada con productos u otros elementos.
 */
public class CategoryAssociatedException extends BaseBusinessException {
    
    public CategoryAssociatedException() {
        super(ErrorCode.CATEGORY_ASSOCIATED);
    }
    
    public CategoryAssociatedException(Long id) {
        super(ErrorCode.CATEGORY_ASSOCIATED, 
              String.format("La categoría con ID %d no se puede eliminar porque está asociada con productos", id));
    }
    
    public CategoryAssociatedException(String name) {
        super(ErrorCode.CATEGORY_ASSOCIATED, 
              String.format("La categoría '%s' no se puede eliminar porque está asociada con productos", name));
    }
}
