package com.products_management.infraestructure.input.rest.model.response;

import lombok.*;

/**
 * Clase de respuesta que representa una categoría en la API REST.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResponse {

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
     * Identificador de la devolución asociada a la categoría.
     */
    private Long returnId;

    /**
     * Estado de la categoría (activo/inactivo).
     */
    private String state;
}
