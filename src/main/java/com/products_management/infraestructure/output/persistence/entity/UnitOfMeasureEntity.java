package com.products_management.infraestructure.output.persistence.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Entidad que representa una unidad de medida en la base de datos.
 */
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "unitOfMeasure")
public class UnitOfMeasureEntity {

    @Schema(description = "Identificador único de la unidad de medida")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Schema(description = "Nombre de la unidad de medida")
    private String name;
    @Schema(description = "Descripción de la unidad de medida")
    private String description;
    @Schema(description = "Abreviatura de la unidad de medida")
    private String abbreviation;
    @Schema(description = "Identificador de la empresa a la que pertenece la unidad de medida")
    private String enterpriseId;
    @Schema(description = "Estado de la unidad de medida")
    private String state;

}
