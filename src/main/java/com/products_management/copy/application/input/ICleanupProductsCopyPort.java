package com.products_management.copy.application.input;

/**
 * Puerto de entrada para limpiar registros temporales de un proceso de copia terminado.
 * REQ-CONTRACT-01 (DELETE /{idProceso}/cleanup).
 */
public interface ICleanupProductsCopyPort {

    /**
     * Elimina los registros de copy_job_log para el proceso dado.
     *
     * @param idProceso UUID del proceso como String
     */
    void limpiar(String idProceso);
}
