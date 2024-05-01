package com.products_management.infraestructure.input.rest.model.response;

import lombok.*;

import java.util.Date;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    private Long id;
    private String code;
    private String itemType;
    private String description;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer taxPercentage;
    private Date creationDate;
    private Long unitOfMeasureId;
    private Long supplierId;
    private Long categoryId;
    private String enterpriseId;
    private double price;
    private String state;
}
