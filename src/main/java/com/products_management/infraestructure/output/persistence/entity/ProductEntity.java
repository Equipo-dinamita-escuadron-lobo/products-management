package com.products_management.infraestructure.output.persistence.entity;

import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.TenantId;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    private String name;
    private String description;
    private Integer quantity;
    @ElementCollection
    @CollectionTable(name = "product_tax_percentage", joinColumns = @JoinColumn(name = "product_id"))
    @Column(name = "tax_percentage")
    private List<String> taxPercentage;
    private Date creationDate;
    private Long unitOfMeasureId;
    private Long categoryId;
    private String enterpriseId;
    private double cost;

    private boolean state;

    private String reference;
    private Long productTypeId;
    private String presentation;
    
    @UpdateTimestamp
    private Instant lastModifiedDate;

    @TenantId
    private String tenantId;

}
