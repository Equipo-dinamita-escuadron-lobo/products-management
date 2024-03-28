package com.products.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    private Long id;
    private String itemType;
    private String code;
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
