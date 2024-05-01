package com.products_management.infraestructure.input.rest.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @JsonIgnore
    private Long id;

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

    @NotNull(message = "Id Unidad de medida es requerida")
    private Long unitOfMeasureId;

    @NotNull(message = "Id Proveedor es requerido")
    private Long supplierId;

    @NotNull(message = "Id Categoria es requerida")
    private Long categoryId;

    @NotNull(message = "Id Empresa es requerido")
    private String enterpriseId;

    @NotNull(message = "Precio es requerido")
    private double price;

    @JsonIgnore
    private String state;
}
