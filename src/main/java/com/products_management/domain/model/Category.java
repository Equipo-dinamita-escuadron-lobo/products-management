package com.products_management.domain.model;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    private Long id;
    private String name;
    private String description;
    private Long enterpriseId;
    private Long inventoryId;
    private Long costId;
    private Long saleId;
    private Long returnId;
}
