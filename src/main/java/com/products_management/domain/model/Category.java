package com.products_management.domain.model;

import java.util.List;

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
     * @brief Lista de impuestos aplicables a la categoría.
     */
    private List<Long> taxes;

    /**
     * Estado de la categoría (true si está activa, false si está inactiva).
     */
    @Builder.Default
    private boolean state = true;

}
