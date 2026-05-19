package com.products_management.copy.application.input;

import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyCancelResponseDto;

/**
 * Puerto de entrada para cancelar un proceso de copia de productos en curso.
 * REQ-CONTRACT-01 (POST /{idProceso}/cancel).
 */
public interface ICancelProductsCopyPort {

    /**
     * Cancela el proceso de copia identificado por idProceso.
     *
     * @param idProceso UUID del proceso como String
     * @return DTO con el estado CANCELADO
     */
    CopyCancelResponseDto cancelar(String idProceso);
}
