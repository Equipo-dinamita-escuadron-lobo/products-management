package com.products_management.copy.application.input;

import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyPhaseRequestDto;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyPhaseResponseDto;

/**
 * Puerto de entrada para ejecutar una fase del proceso de copia de productos.
 * REQ-PRODUCTS-04, REQ-PRODUCTS-05.
 */
public interface IExecuteProductsCopyPhasePort {

    /**
     * Ejecuta la fase de copia indicada en el request, copiando
     * UnitOfMeasure → ProductType → Category → Product con tenant override
     * y soporte de idempotencia.
     *
     * @param request datos de la fase a ejecutar
     * @return resultado con equivalencias generadas y estado de la copia
     */
    CopyPhaseResponseDto ejecutar(CopyPhaseRequestDto request);
}
