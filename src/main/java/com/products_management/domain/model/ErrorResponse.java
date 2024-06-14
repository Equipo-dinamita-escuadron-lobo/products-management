package com.products_management.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Clase que representa una respuesta de error utilizada para comunicar errores o excepciones en la aplicación.
 */
@Builder
@Getter
@Setter
public class ErrorResponse {

    /**
     * Código que identifica el tipo de error.
     */
    private String code;

    /**
     * Mensaje descriptivo del error.
     */
    private String message;

    /**
     * Detalles adicionales del error, como una lista de strings.
     */
    private List<String> details;

    /**
     * Fecha y hora en la que ocurrió el error.
     */
    private LocalDateTime timestamp;

}
