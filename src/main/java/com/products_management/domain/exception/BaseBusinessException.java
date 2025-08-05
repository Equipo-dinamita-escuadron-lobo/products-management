package com.products_management.domain.exception;

/**
 * Excepción base para todas las excepciones de negocio del dominio.
 * Proporciona funcionalidad común para manejo de códigos de error y mensajes.
 */
public abstract class BaseBusinessException extends RuntimeException {
    
    private final ErrorCode errorCode;
    
    protected BaseBusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
    
    protected BaseBusinessException(ErrorCode errorCode, String customMessage) {
        super(customMessage);
        this.errorCode = errorCode;
    }
    
    protected BaseBusinessException(ErrorCode errorCode, String customMessage, Throwable cause) {
        super(customMessage, cause);
        this.errorCode = errorCode;
    }
    
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}