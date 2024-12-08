package com.products_management.infraestructure.output.persistence.entity;

import lombok.*;

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

@Entity
@Table(name = "product_type")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductTypeEntity {

    @Schema(description = "Identificador único del tipo de producto")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nombre del tipo de producto")
    private String name;    
    @Schema(description = "Descripción del tipo de producto")
    private String description;
    @Schema(description = "Identificador de la empresa a la que pertenece el tipo de producto")
    private String enterpriseId;
}
