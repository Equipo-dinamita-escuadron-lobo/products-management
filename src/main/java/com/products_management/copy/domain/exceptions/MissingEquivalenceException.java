package com.products_management.copy.domain.exceptions;

/**
 * Excepción lanzada cuando una FK cross-servicio obligatoria no tiene equivalencia
 * en equivalenciasPrev recibido del orquestador.
 * REQ-EQUIVPREV-02, ADR-30.
 */
public class MissingEquivalenceException extends RuntimeException {

    /**
     * @param tabla    nombre de la tabla/módulo (ej. "account")
     * @param idViejo  ID del registro en entOrigen sin equivalencia
     * @param campo    nombre del campo FK (ej. "inventoryId")
     */
    public MissingEquivalenceException(String tabla, String idViejo, String campo) {
        super(String.format(
            "No se encontró equivalencia para tabla='%s', idViejo='%s' en el campo '%s'",
            tabla, idViejo, campo
        ));
    }
}
