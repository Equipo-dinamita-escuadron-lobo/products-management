package com.products_management.copy.domain.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Modelo de dominio que representa la equivalencia entre el ID de una entidad en
 * entOrigen y el nuevo ID generado en entDestino.
 * REQ-PRODUCTS-04, REQ-EQUIVPREV-02.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CopyEquivalencia {

    /** Nombre de la tabla/módulo de origen (ej. "account", "tax", "category"). */
    private String tabla;

    /** ID antiguo (en entOrigen) como String para uniformidad. */
    private String idViejo;

    /** ID nuevo (en entDestino) como String. */
    private String idNuevo;
}
