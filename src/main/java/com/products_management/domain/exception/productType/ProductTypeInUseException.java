package com.products_management.domain.exception.productType;

import com.products_management.domain.exception.BaseBusinessException;
import com.products_management.domain.exception.ErrorCode;

/**
 * @brief Excepción para tipo de producto con productos en uso
 *
 * Se lanza cuando se intenta editar un tipo de producto que contiene productos
 * que ya han sido utilizados, impidiendo la modificación por integridad de datos.
 */
public class ProductTypeInUseException extends BaseBusinessException {

    public ProductTypeInUseException() {
        super(ErrorCode.PRODUCT_TYPE_IN_USE);
    }
}
