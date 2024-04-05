package com.products_management.infraestructure.input.rest.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "Codigo es requerido")
    private String code;

    @NotBlank(message = "Nombre es requerido")
    private String itemType;

    @NotBlank(message = "Descripcion es requerida")
    private String description;

    @NotNull(message = "Cantidad minima es requerida")
    private Integer minQuantity;

    @NotNull(message = "Cantidad maxima es requerida")
    private Integer maxQuantity;

    @NotNull(message = "Porcentaje de impuesto es requerido")
    private Integer taxPercentage;

    @NotNull(message = "Fecha de creacion es requerida")
    private Date creationDate;

    @NotBlank(message = "Unidad de medida es requerida")
    private String unitOfMeasure;

    @NotBlank(message = "Proveedor es requerido")
    private String supplier;

    @NotBlank(message = "Categoria es requerida")
    private String category;

    @NotNull(message = "Precio es requerido")
    private double price;
}
