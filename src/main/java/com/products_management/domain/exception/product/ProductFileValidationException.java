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

    public static ProductFileValidationException forNullFile() {
        return new ProductFileValidationException("El archivo no puede ser null");
    }

    public static ProductFileValidationException forEmptyFile(String fileName) {
        return new ProductFileValidationException(
            String.format("El archivo '%s' está vacío", fileName)
        );
    }

    public static ProductFileValidationException forInvalidExtension(String fileName, String[] supportedExtensions) {
        return new ProductFileValidationException(
            String.format("El archivo '%s' tiene una extensión no válida. Extensiones soportadas: %s",
                fileName, String.join(", ", supportedExtensions))
        );
    }

    public static ProductFileValidationException forInvalidMimeType(String fileName, String actualMimeType, String[] supportedMimeTypes) {
        return new ProductFileValidationException(
            String.format("El archivo '%s' tiene un tipo MIME no válido (%s). Tipos soportados: %s",
                fileName, actualMimeType, String.join(", ", supportedMimeTypes))
        );
    }
}