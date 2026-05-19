package com.products_management.copy.domain.exceptions;

/**
 * Excepción lanzada al intentar registrar un trabajo de copia que ya existe
 * para la combinación (idProceso, fase, modulo).
 * REQ-PRODUCTS-05 — idempotencia.
 */
public class DuplicateCopyJobException extends RuntimeException {

    /**
     * @param idProceso identificador del proceso
     * @param fase      número de fase
     */
    public DuplicateCopyJobException(String idProceso, int fase) {
        super(String.format(
            "Ya existe un job de copia para el proceso '%s' en la fase %d",
            idProceso, fase
        ));
    }
}
