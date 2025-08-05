package com.products_management.domain.exception;

/**
 * Excepción lanzada cuando se intenta eliminar un tipo de producto que está asociado con productos.
 */
public class ProductTypeAssociatedException extends BaseBusinessException {
    
    public ProductTypeAssociatedException() {
        super(ErrorCode.PRODUCT_TYPE_ASSOCIATED);
    }
    
    public ProductTypeAssociatedException(Long id) {
        super(ErrorCode.PRODUCT_TYPE_ASSOCIATED, 
              String.format("El tipo de producto con ID %d no se puede eliminar porque está asociado con productos", id));
    }
    
    public ProductTypeAssociatedException(String name) {
        super(ErrorCode.PRODUCT_TYPE_ASSOCIATED, 
              String.format("El tipo de producto '%s' no se puede eliminar porque está asociado con productos", name));
    }
}
