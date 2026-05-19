package com.products_management.copy.infraestructure.adapters.input.rest.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO de response para la consulta de estado de un proceso de copia.
 * REQ-CONTRACT-01 (GET /{idProceso}/status).
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyStatusResponseDto {

    private Integer fase;
    private String estado;
    private Integer registrosProcesados;
    private Integer intentos;
    private String ultimoError;
}
