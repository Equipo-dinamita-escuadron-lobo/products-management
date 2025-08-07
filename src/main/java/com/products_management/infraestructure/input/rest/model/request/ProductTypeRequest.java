package com.products_management.infraestructure.input.rest.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

/**
 * Clase que representa la solicitud para crear o actualizar un tipo de producto.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeRequest {
    private String name;
    private String description;
    private String enterpriseId;
    
    @JsonIgnore
    @Builder.Default
    private boolean state = true; // Por defecto activo
}
