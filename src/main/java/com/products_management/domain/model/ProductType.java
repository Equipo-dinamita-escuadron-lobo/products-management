package com.products_management.domain.model;


import lombok.*;

/**
 * Clase que representa un tipo de producto en el sistema.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductType {

    /**
     * Identificador único del tipo de producto.
     */
    private Long id;

    /**
     * Nombre del tipo de producto.
     */
    private String name;

    /**
     * Descripción del tipo de producto.
     */
    private String description;

    /**
     * Identificador de la empresa a la que pertenece el tipo de producto.
     */
    private String enterpriseId;

}
