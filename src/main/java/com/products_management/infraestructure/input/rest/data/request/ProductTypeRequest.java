package com.products_management.infraestructure.input.rest.data.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @NotBlank
    @NotNull(message = "El nombre es requerido")
    private String name;

    @NotBlank
    @NotNull(message = "La descripcion es requerida")
    private String description;

    @NotBlank
    @NotNull(message = "El id de empresa es requerido")
    private String enterpriseId;
    
    @JsonIgnore
    @Builder.Default
    private boolean state = true; 
}
