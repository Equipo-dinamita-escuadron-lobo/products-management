package com.products_management.domain.model;

import com.products_management.domain.enums.ImportErrorType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Clase que representa un detalle de error durante la importación.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ImportErrorDetail {

    /**
     * Número de fila donde ocurrió el error (1-indexed).
     */
    private Integer rowNumber;

    /**
     * Número de columna donde ocurrió el error (1-indexed).
     */
    private Integer columnNumber;

    /**
     * Nombre de la columna donde ocurrió el error.
     */
    private String columnName;

    /**
     * Código del error.
     */
    private String errorCode;

    /**
     * Mensaje descriptivo del error.
     */
    private String errorMessage;

    /**
     * Tipo de error.
     */
    private ImportErrorType errorType;

    /**
     * Valor del campo que causó el error.
     */
    private String fieldValue;
}