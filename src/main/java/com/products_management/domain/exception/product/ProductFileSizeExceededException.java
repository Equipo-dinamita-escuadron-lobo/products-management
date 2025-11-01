package com.products_management.domain.exception.product;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción lanzada cuando un archivo excede el tamaño máximo permitido.
 */
public class ProductFileSizeExceededException extends BaseBusinessException {

    /**
     * Crea una excepción indicando el tamaño máximo permitido.
     *
     * @param maxSize Tamaño máximo permitido en bytes
     */
    public ProductFileSizeExceededException(long maxSize) {
        super(
            ErrorCode.FILE_VALIDATION_ERROR,
            String.format("El archivo excede el tamaño máximo permitido de %s.",
                formatFileSize(maxSize))
        );
    }

    /**
     * Formatea el tamaño del archivo en unidades legibles (KB, MB).
     */
    private static String formatFileSize(long sizeInBytes) {
        if (sizeInBytes < 1024) {
            return sizeInBytes + " bytes";
        } else if (sizeInBytes < 1024 * 1024) {
            return String.format("%.2f KB", sizeInBytes / 1024.0);
        } else {
            return String.format("%.2f MB", sizeInBytes / (1024.0 * 1024.0));
        }
    }
}