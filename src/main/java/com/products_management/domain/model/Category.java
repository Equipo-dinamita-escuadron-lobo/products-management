package com.products_management.domain.model;

import lombok.*;

/**
 * Clase que representa una categoría de productos.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    /**
     * Identificador único de la categoría.
     */
    private Long id;

    /**
     * Nombre de la categoría.
     */
    private String name;

    /**
     * Descripción de la categoría.
     */
    private String description;

    /**
     * Identificador de la empresa a la que pertenece la categoría.
     */
    private String enterpriseId;

    /**
     * Identificador del inventario asociado a la categoría.
     */
    private Long inventoryId;

    /**
     * Identificador del costo asociado a la categoría.
     */
    private Long costId;

    /**
     * Identificador de la venta asociada a la categoría.
     */
    private Long saleId;

    /**
     * Identificador del retorno asociado a la categoría.
     */
    private Long returnId;

    /**
     * Estado de la categoría ("true" si está activa, "false" si está inactiva).
     */
    private String state;

     /**
     * ID de impuesto a la categoria
     */
    private Long taxId;

}
