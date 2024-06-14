package com.products_management.domain.model;

import lombok.*;

/**
 * Clase que representa una unidad de medida utilizada en el sistema.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasure {

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
     * Estado de la unidad de medida ("true" si está activa, "false" si está inactiva).
     */
    private String state;
}
