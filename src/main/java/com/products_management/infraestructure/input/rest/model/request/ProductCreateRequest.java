package com.products_management.infraestructure.input.rest.model.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @JsonIgnore
    private String code;

    @NotBlank(message = "Nombre es requerido")
    private String name;

    @NotBlank(message = "Descripción es requerida")
    private String description;

    @NotNull(message = "Cantidad es requerida")
    private Integer quantity;

    @NotNull(message = "Porcentaje de impuesto es requerido")
    private Integer taxPercentage;

    @NotNull(message = "Fecha de creación es requerida")
    private Date creationDate;

    @NotNull(message = "Id de la unidad de medida es requerido")
    private Long unitOfMeasureId;

    @NotNull(message = "Id de la categoría es requerido")
    private Long categoryId;

    @NotNull(message = "Id de la empresa es requerido")
    private String enterpriseId;

    @NotNull(message = "Costo es requerido")
    private double cost;

    @JsonIgnore
    private String state;

    private String reference;

    private String presentation;
    
    // Campo opcional para la relación con ProductType
    private Long productTypeId; // Este campo es opcional

}
