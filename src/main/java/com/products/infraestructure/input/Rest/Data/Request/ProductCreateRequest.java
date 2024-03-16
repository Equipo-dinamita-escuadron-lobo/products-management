package com.products.infraestructure.input.Rest.Data.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductCreateRequest {
//TODO: analizar si es necesario agregar todos los campos

    @NotBlank(message = "ID es requerido")
    private String id;

    @NotBlank(message = "Nombre es requerido")
    private String itemType;

    @NotBlank(message = "Codigo es requerido")
    private String code;

    @NotBlank(message = "Descripticion es requerida")
    private String description;

    @NotBlank(message = "Cantidad minima es requerida")
    private Integer minQuantity;

    @NotBlank(message = "Cantidad maxima es requerida")
    private Integer maxQuantity;

    @NotBlank(message = "porcentaje de impuesto es requerido")
    private Integer taxPercentage;

    @NotBlank(message = "Fecha de creacion es requerida")
    private Date creationDate;

    @NotBlank(message = "Unidad de medida es requerida")
    private String unitOfMeasure;

    @NotBlank(message = "Proveedor es requerido")
    private String supplier;

    @NotBlank(message = "Categoria es requerida")
    private String category;

    @NotBlank(message = "Precio es requerido")
    private Double price;
}
