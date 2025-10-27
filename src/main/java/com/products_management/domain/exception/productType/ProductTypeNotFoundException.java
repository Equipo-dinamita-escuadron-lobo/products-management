package com.products_management.domain.exception.productType;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando no se encuentra un tipo de producto específico.
 */
public class ProductTypeNotFoundException extends BaseBusinessException {
    
    public ProductTypeNotFoundException() {
        super(ErrorCode.PRODUCT_TYPE_NOT_FOUND);
    }
    
    public ProductTypeNotFoundException(Long id) {
        super(ErrorCode.PRODUCT_TYPE_NOT_FOUND, 
              String.format("No se encontró el tipo de producto con ID %d", id));
    }
    
    public ProductTypeNotFoundException(String enterpriseId) {
        super(ErrorCode.PRODUCT_TYPE_NOT_FOUND, 
              String.format("No se encontraron tipos de producto para la empresa con ID %s", enterpriseId));
    }
}
