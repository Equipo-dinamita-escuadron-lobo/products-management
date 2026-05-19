package com.products_management.copy.domain.models;

import com.products_management.copy.domain.enums.CopyEstado;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

/**
 * Modelo de dominio que representa el registro de idempotencia de un trabajo de copia.
 * Cada combinación (idProceso + fase + modulo) debe ser única.
 * REQ-PRODUCTS-05.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyJobLog {

    /** Identificador del proceso generado por el orquestador (enterprises-management). */
    private UUID idProceso;

    /** Número de fase ejecutada. */
    private Integer fase;

    /** Nombre del módulo participante — siempre "products". */
    private String modulo;

    /** Estado actual del trabajo. */
    private CopyEstado estado;

    /** Momento de inicio de la ejecución. */
    private Instant fechaInicio;

    /** Momento de fin de la ejecución (null si aún no terminó). */
    private Instant fechaFin;

    /** Cantidad de registros procesados en esta fase. */
    @Builder.Default
    private Integer equivalenciasGeneradas = 0;

    /** Mensaje de error en caso de fallo. */
    private String errorMessage;
}
