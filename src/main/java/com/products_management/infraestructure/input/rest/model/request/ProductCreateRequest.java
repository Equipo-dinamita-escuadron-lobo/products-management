package com.products_management.infraestructure.input.rest.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;

/**
 * Clase de solicitud utilizada para crear un producto en el sistema.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductCreateRequest {

    @JsonIgnore
    private Long id;

    @NotBlank(message = "Código es requerido")
    private String code;

    @NotBlank(message = "Tipo de ítem es requerido")
    private String itemType;

    @NotBlank(message = "Descripción es requerida")
    private String description;

    @NotNull(message = "Cantidad mínima es requerida")
    private Integer minQuantity;

    @NotNull(message = "Cantidad máxima es requerida")
    private Integer maxQuantity;

    @NotNull(message = "Porcentaje de impuesto es requerido")
    private Integer taxPercentage;

    @NotNull(message = "Fecha de creación es requerida")
    private Date creationDate;

    @NotNull(message = "Id de la unidad de medida es requerido")
    private Long unitOfMeasureId;

    @NotNull(message = "Id del proveedor es requerido")
    private Long supplierId;

    @NotNull(message = "Id de la categoría es requerido")
    private Long categoryId;

    @NotNull(message = "Id de la empresa es requerido")
    private String enterpriseId;

    @NotNull(message = "Precio es requerido")
    private double price;

    @JsonIgnore
    private String state;

    private String reference;
}
