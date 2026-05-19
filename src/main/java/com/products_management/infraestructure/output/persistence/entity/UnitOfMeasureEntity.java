package com.products_management.infraestructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * @brief Entidad JPA para persistencia de unidades de medida
 *
 * Representa la tabla de unidades de medida en base de datos,
 * definiendo abreviaturas y nombres para cuantificación de productos.
 */
@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "unitOfMeasure")
public class UnitOfMeasureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String abbreviation;
    private String enterpriseId;
    private boolean state;

    /**
     * Fecha de creación usada como corte para snapshot de copia.
     * Se asigna automáticamente al insertar (REQ-PRODUCTS-03).
     */
    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();

}
