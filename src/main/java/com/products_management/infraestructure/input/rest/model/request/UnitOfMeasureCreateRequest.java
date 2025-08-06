package com.products_management.infraestructure.input.rest.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * Clase de solicitud utilizada para crear una unidad de medida en el sistema.
 */
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

    @NotBlank(message = "Descripción es requerida")
    private String description;

    @NotBlank(message = "Abreviación es requerida")
    private String abbreviation;

    @NotBlank(message = "Id de la empresa es requerido")
    private String enterpriseId;

    @JsonIgnore
    @Builder.Default
    private boolean state = true; // Por defecto activo

    public UnitOfMeasureCreateRequest(String name, String description, String abbreviation, String enterpriseId, boolean state) {
        this.name = name;
        this.description = description;
        this.abbreviation = abbreviation;
        this.enterpriseId = enterpriseId;
        this.state = state;
    }
}
