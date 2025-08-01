package com.products_management.infraestructure.output.persistence.entity;

import java.util.Date;

import org.hibernate.annotations.TenantId;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
/**
 * Entidad que representa un producto en la base de datos.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "product")
public class ProductEntity {

    @Schema(description = "Identificador único del producto")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Código del producto")
    private String code;
    @Schema(description = "Nombre del producto")
    private String name;
    @Schema(description = "Descripción del producto")
    private String description;
    @Schema(description = "Precio de venta del producto")
    private Integer quantity;
    @Schema(description = "Porcentaje de impuestos del producto")
    private Integer taxPercentage;
    @Schema(description = "Fecha de creación del producto")
    private Date creationDate;
    @Schema(description = "Identificador de la unidad de medida del producto")
    private Long unitOfMeasureId;
    @Schema(description = "Identificador de la categoría del producto")
    private Long categoryId;
    @Schema(description = "Identificador de la empresa a la que pertenece el producto")
    private String enterpriseId;
    @Schema(description = "Costo del producto")
    private double cost;
    @Schema(description = "Estado del producto")
    private String state;
    @Schema(description = "Referencia del producto")
    private String reference;
    @Schema(description = "Identificador del tipo de producto")
    private Long productTypeId;
    @Schema(description = "Identificador del inventario del producto")
    private Long inventoryId;

    @Schema(description = "Presentación del producto")
    private String presentation;
    
    @TenantId
    private String tenantId;

}
