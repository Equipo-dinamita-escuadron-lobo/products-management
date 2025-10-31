package com.products_management.domain.exception.product;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * Excepción específica para errores durante la exportación de productos.
 * Extiende BaseBusinessException para mantener consistencia con el manejo de errores del dominio.
 */
public class ProductExportException extends BaseBusinessException {

    /**
     * Constructor con código de error y mensaje.
     *
     * @param errorCode código específico del error
     * @param message mensaje descriptivo del error
     */
    public ProductExportException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    /**
     * Constructor con código de error, mensaje y causa.
     *
     * @param errorCode código específico del error
     * @param message mensaje descriptivo del error
     * @param cause causa raíz del error
     */
    public ProductExportException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    /**
     * Constructor de conveniencia para exportación sin datos.
     *
     * @param status filtro de estado aplicado (true=activos, false=inactivos, null=todos)
     * @return nueva instancia de ProductExportException
     */
    public static ProductExportException forNoData(Boolean status) {
        String message;
        if (status == null) {
            message = "No hay productos para exportar";
        } else if (status) {
            message = "No hay productos activos para exportar";
        } else {
            message = "No hay productos inactivos para exportar";
        }

        return new ProductExportException(
            ErrorCode.PRODUCT_EXPORT_NO_DATA,
            message
        );
    }
}
