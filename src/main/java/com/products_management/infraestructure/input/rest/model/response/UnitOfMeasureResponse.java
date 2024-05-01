package com.products_management.infraestructure.input.rest.model.response;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasureResponse {
    private Long id;
    private String name;
    private String description;
    private String abbreviation;
    private String enterpriseId;
    private String state;
}
