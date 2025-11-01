package com.products_management.domain.exception.product;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción específica para errores de validación de archivos en importación de productos.
 */
public class ProductFileValidationException extends BaseBusinessException {

    public ProductFileValidationException(String message) {
        super(ErrorCode.FILE_VALIDATION_ERROR, message);
    }

    public ProductFileValidationException(String message, Throwable cause) {
        super(ErrorCode.FILE_VALIDATION_ERROR, message, cause);
    }
}