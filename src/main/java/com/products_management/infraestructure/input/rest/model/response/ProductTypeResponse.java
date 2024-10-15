package com.products_management.infraestructure.input.rest.model.response;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa la respuesta para un tipo de producto.
 */
@Getter
@Setter
public class ProductTypeResponse {
    private Long id;
    private String name;
    private String description;
    private String enterpriseId;
}
