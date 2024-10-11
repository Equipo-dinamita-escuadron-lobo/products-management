package com.products_management.infraestructure.input.rest.model.request;

import lombok.Getter;
import lombok.Setter;

/**
 * Clase que representa la solicitud para crear o actualizar un tipo de producto.
 */
@Getter
@Setter
public class ProductTypeRequest {
    private String name;
    private String description;
    private long enterpriseId;
}
