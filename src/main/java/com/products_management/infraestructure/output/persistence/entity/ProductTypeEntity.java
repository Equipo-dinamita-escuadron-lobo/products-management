package com.products_management.infraestructure.output.persistence.entity;

import lombok.*;

import jakarta.persistence.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Entity
@Table(name = "product_type")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;    
    private String description;
    private String enterpriseId;

    /**
     * Fecha de creación usada como corte para snapshot de copia.
     * REQ-PRODUCTS-03.
     */
    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
