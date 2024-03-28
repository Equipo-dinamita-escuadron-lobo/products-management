package com.products.infraestructure.input.Rest.Data.Request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ProductRequest {
//TODO: analizar si es necesario agregar todos los campos
    @JsonIgnore
    private String id;

    @NotBlank(message = "Nombre es requerido")
    private String itemType;

    @NotBlank(message = "Codigo es requerido")
    @NotNull(message = "El codigo es nulo")
    private String code;

    @NotBlank(message = "Descripticion es requerida")
    @NotNull(message = "la descripcion es nula")
    @Size(min = 4, max = 100, message = "La descripcion debe estar en el rango de 4 a 100 caracteres")
    private String description;

    @NotNull(message = "la cantidad minima es nulo")
    private Integer minQuantity;

    @NotNull(message = "la cantidad maxima es nulo")
    private Integer maxQuantity;

    @NotNull(message = "porcentaje de impuesto es nulo")
    private Integer taxPercentage;

    @NotNull(message = "Fecha de creacion es nulo")
    private Date creationDate;

    @NotBlank(message = "Unidad de medida es requerida")
    @NotNull(message = "Unidad de medida es nulo")
    private String unitOfMeasure;

    @NotBlank(message = "Proveedor es requerido")
    @NotNull(message = "Proveedor es nulo")
    private String supplier;

    @NotBlank(message = "Categoria es requerida")
    @NotNull(message = "Categoria es nula")
    private String category;

    @NotNull(message = "Precio es nulo")
    @Min(value = 0, message="El precio no puede ser negativo")
    private Double price;
}
