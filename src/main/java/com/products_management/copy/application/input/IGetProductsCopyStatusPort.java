package com.products_management.copy.application.input;

import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyStatusResponseDto;

/**
 * Puerto de entrada para consultar el estado de un proceso de copia de productos.
 * REQ-CONTRACT-01 (GET /{idProceso}/status).
 */
public interface IGetProductsCopyStatusPort {

    /**
     * Obtiene el estado más reciente del proceso de copia para el idProceso dado.
     *
     * @param idProceso UUID del proceso como String
     * @return DTO con estado, fase y registros procesados
     */
    CopyStatusResponseDto obtenerEstado(String idProceso);
}
