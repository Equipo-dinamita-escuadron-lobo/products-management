package com.products_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UnitOfMeasure {
    private Long id;
    private String name;
    private String description;
    private String abbreviation;
    private String enterpriseId;
    private String state;
}
