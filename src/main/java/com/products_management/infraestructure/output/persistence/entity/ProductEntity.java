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

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String code;
    private String itemType;
    private String description;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer taxPercentage;
    private Date creationDate;
    private Long unitOfMeasureId;
    private Long categoryId;
    private String enterpriseId;
    private double price;
    private String state;

    @TenantId
    private String tenantId;
}
