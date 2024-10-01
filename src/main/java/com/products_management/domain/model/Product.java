package com.products_management.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.Date;

/**
 * Clase que representa un producto en el sistema.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    /**
     * Identificador único del producto.
     */
    private Long id;

    /**
     * Código del producto.
     */
    private String code;

    /**
     * Tipo de artículo del producto.
     */
    private String itemType;

    /**
     * Descripción del producto.
     */
    private String description;

    /**
     * Cantidad mínima requerida del producto.
     */
    private Integer minQuantity;

    /**
     * Cantidad máxima permitida del producto.
     */
    private Integer maxQuantity;

    /**
     * Porcentaje de impuestos aplicable al producto.
     */
    private Integer taxPercentage;

    /**
     * Fecha de creación del producto.
     */
    private Date creationDate;

    /**
     * Identificador de la unidad de medida del producto.
     */
    private Long unitOfMeasureId;

    /**
     * Identificador de la categoría del producto.
     */
    private Long categoryId;

    /**
     * Identificador de la empresa a la que pertenece el producto.
     */
    private String enterpriseId;

    /**
     * Precio del producto.
     */
    private double price;

    /**
     * Estado del producto ("true" si está activo, "false" si está inactivo).
     */
    private String state;

    /**
     * Genera un código único basado en el tipo de ítem, categoría y ID del producto.
     */
    public void generateCode() {
        String itemTypePrefix = itemType != null && itemType.length() >= 3 ? itemType.substring(0, 3).toUpperCase() : "UNK";
        this.code = String.format("%s-%d-%d", itemTypePrefix, categoryId, id);
    }
}
