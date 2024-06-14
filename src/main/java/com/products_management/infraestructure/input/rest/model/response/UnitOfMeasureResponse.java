package com.products_management.infraestructure.input.rest.model.response;

import lombok.*;

/**
 * Clase de respuesta que representa una unidad de medida en la API REST.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasureResponse {

    /**
     * Identificador único de la unidad de medida.
     */
    private Long id;

    /**
     * Nombre de la unidad de medida.
     */
    private String name;

    /**
     * Descripción de la unidad de medida.
     */
    private String description;

    /**
     * Abreviatura de la unidad de medida.
     */
    private String abbreviation;

    /**
     * Identificador de la empresa a la que pertenece la unidad de medida.
     */
    private String enterpriseId;

    /**
     * Estado de la unidad de medida (activo/inactivo).
     */
    private String state;
}
