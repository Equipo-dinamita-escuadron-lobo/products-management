package com.products_management.copy.domain.enums;

/**
 * Estados posibles de un trabajo de copia de entidades maestras.
 * Replica el contrato uniforme del orquestador (REQ-CONTRACT-03).
 */
public enum CopyEstado {

    /** El proceso está en ejecución actualmente. */
    EN_PROCESO,

    /** Copia completada sin advertencias. */
    COMPLETADO,

    /** Copia completada pero con advertencias (FKs no resueltas, etc.). */
    COMPLETADO_CON_ADVERTENCIAS,

    /** Fallo recuperable — el orquestador puede reintentar. */
    FALLIDO,

    /** Fallo definitivo — no se debe reintentar. */
    ERROR_NO_REINTENTABLE,

    /** Proceso cancelado por el orquestador. */
    CANCELADO
}
