package com.products_management.infraestructure.input.rest.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Clase de solicitud utilizada para crear una categoría en el sistema.
 */
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryCreateRequest {

    @JsonIgnore
    private Long id;

    @NotBlank(message = "Nombre es requerido")
    private String name;

    @NotBlank(message = "Descripción es requerida")
    private String description;

    @NotNull(message = "Id de la empresa es requerido")
    private String enterpriseId;

    @NotNull(message = "Id del inventario es requerido")
    private Long inventoryId;

    @NotNull(message = "Id del costo es requerido")
    private Long costId;

    @NotNull(message = "Id de la venta es requerido")
    private Long saleId;

    @NotNull(message = "Id de la devolución es requerido")
    private Long returnId;

    @JsonIgnore
    private String state;
}
