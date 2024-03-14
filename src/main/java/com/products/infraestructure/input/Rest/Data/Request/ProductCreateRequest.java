package com.products.infraestructure.input.Rest.Data.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductCreateRequest {

    @NotBlank(message = "ID es requerido")
    private String id;

    @NotBlank(message = "Nombre es requerido")
    private String name;

    @NotBlank(message = "Descripticion es requerida")
    private String description;

    @NotBlank(message = "Precio es requerido")
    private Double price;
}
