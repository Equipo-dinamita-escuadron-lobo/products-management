package com.products_management.infraestructure.input.rest.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasureCreateRequest {
    @JsonIgnore
    private Long id;
    @NotBlank(message = "Nombre es requerido")
    private String name;
    @NotBlank(message = "Descripcion es requerida")
    private String description;
    @NotBlank(message = "Abreviacion es requerida")
    private String abbreviation;
}
