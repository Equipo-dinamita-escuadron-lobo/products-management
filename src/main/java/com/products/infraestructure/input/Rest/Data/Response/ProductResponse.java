package com.products.infraestructure.input.Rest.Data.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {
    private Long id;
    private String itemType;
    private String description;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer taxPercentage;
    private Date creationDate;
    private String unitOfMeasure;//TODO cambiar tipo de dato por unidad de medida
    private String supplier; //TODO cambiar tipo de dato por lista de proveedores
    private String category; //TODO cambiar tipo de dato por lista de categorias
    private double price;
}