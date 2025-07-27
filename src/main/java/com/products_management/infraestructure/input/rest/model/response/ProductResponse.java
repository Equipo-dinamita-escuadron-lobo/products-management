package com.products_management.infraestructure.input.rest.model.response;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Clase de respuesta que representa un producto en la API REST.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductResponse {

    /**
     * Identificador único del producto.
     */
    private Long id;

    /**
     * Código del producto.
     */
    private String code;

    /**
     * Nombre del producto.
     */
    private String name;

    /**
     * Descripción del producto.
     */
    private String description;

    /**
     * Cantidad del producto.
     */
    private Integer quantity;

    /**
     * Porcentaje de impuesto aplicado al producto.
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
     * Costo del producto.
     */
    private double cost;

    /**
     * Estado del producto (activo/inactivo).
     */
    private String state;

    /**
     * Referencia del producto.
     */
    private String reference;

    /**
     * Tipo de producto relacionado.
     */
    private Long productTypeId;


}
