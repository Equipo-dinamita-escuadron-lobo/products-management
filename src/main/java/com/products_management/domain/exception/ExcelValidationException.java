package com.products_management.domain.exception;

/**
 * Excepción lanzada cuando ocurre un error de validación en archivos Excel.
 */
public class ExcelValidationException extends BaseBusinessException {

    public ExcelValidationException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public ExcelValidationException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }
}