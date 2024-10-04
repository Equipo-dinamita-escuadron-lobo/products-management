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
     * Identificador del proveedor del producto.
     */
    private Long supplierId;

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
     * Referencia opcional del producto.
     */
    private String reference;
}
