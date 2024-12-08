package com.products_management.infraestructure.output.persistence.entity;

import org.hibernate.annotations.TenantId;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidad que representa una categoría en la base de datos.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "category")
public class CategoryEntity {

    @Schema(description = "Identificador único de la categoría")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nombre de la categoría")
    private String name;
    @Schema(description = "Descripción de la categoría")
    private String description;
    @Schema(description = "Identificador de la empresa a la que pertenece la categoría")
    private String enterpriseId;
    private Long inventoryId;
    private Long costId;
    private Long saleId;
    private Long returnId;
    @Schema(description = "Estado de la categoría")
    private String state;

    @TenantId
    private String tenantId;

    private Long taxId;
}
