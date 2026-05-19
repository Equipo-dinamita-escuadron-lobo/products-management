package com.products_management.infraestructure.output.persistence.entity;

import java.util.List;

import org.hibernate.annotations.TenantId;

import jakarta.persistence.*;
import lombok.*;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

/**
 * @brief Entidad JPA para persistencia de categorías
 *
 * Representa la tabla de categorías en base de datos con soporte para multitenancy,
 * incluyendo configuración contable y asociaciones con productos.
 */
@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String enterpriseId;
    private Long inventoryId;
    private Long costId;
    private Long saleId;
    private Long returnId;
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "category_taxes", joinColumns = @JoinColumn(name = "category_id"))
    @Column(name = "tax_id")
    private List<Long> taxes;
    private boolean state;

    @TenantId
    private String tenantId;

    private Long taxId;

    /**
     * Fecha de creación usada como corte para snapshot de copia.
     * REQ-PRODUCTS-03.
     */
    @Builder.Default
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt = Instant.now();
}
