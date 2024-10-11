package com.products_management.domain.model;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
     * @brief Identificador único del producto.
     */
    private Long id;

    /**
     * @brief Código del producto.
     */
    private String code;

    /**
     * @brief Tipo de artículo del producto.
     */
    private String itemType;

    /**
     * @brief Descripción del producto.
     */
    private String description;

    /**
     * Cantidad permitida del producto.
     */
    private Integer quantity;

    /**
     * @brief Porcentaje de impuestos aplicable al producto.
     */
    private Integer taxPercentage;

    /**
     * @brief Fecha de creación del producto.
     */
    private Date creationDate;

    /**
     * @brief Identificador de la unidad de medida del producto.
     */
    private Long unitOfMeasureId;

    /**
     * @brief Identificador de la categoría del producto.
     */
    private Long categoryId;

    /**
     * @brief Identificador de la empresa a la que pertenece el producto.
     */
    private String enterpriseId;

    /**
     * Costo del producto.
     */
    private double cost;

    /**
     * @brief Estado del producto ("true" si está activo, "false" si está inactivo).
     */
    private String state;
    /**

     * Campo de referencia opcional
     */
    private String reference;

    private ProductType productType;
 
    /**

     * Genera un código único basado en el tipo de ítem, categoría y ID del producto.
     */
    public void generateCode() {
        String itemTypePrefix = itemType != null && itemType.length() >= 3 ? itemType.substring(0, 3).toUpperCase() : "UNK";
        this.code = String.format("%s-%d-%d", itemTypePrefix, categoryId, id);
    }
}
