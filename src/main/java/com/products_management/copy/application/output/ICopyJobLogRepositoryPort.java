package com.products_management.copy.application.output;

import com.products_management.copy.domain.models.CopyJobLog;

import java.util.Optional;

/**
 * Puerto de salida para persistir y consultar registros de idempotencia de copia.
 * REQ-PRODUCTS-05.
 */
public interface ICopyJobLogRepositoryPort {

    /**
     * Guarda o actualiza un registro de copia.
     *
     * @param log modelo de dominio a persistir
     * @return el registro guardado
     */
    CopyJobLog guardar(CopyJobLog log);

    /**
     * Busca el registro de copia para la combinación idProceso + fase.
     * Usado para verificar idempotencia antes de re-ejecutar.
     */
    Optional<CopyJobLog> buscarPorIdProcesoYFase(String idProceso, int fase);

    /**
     * Busca el registro más reciente del proceso (usado para status y cancel).
     */
    Optional<CopyJobLog> buscarPorIdProceso(String idProceso);

    /**
     * Elimina todos los registros del proceso (cleanup).
     */
    void eliminarPorIdProceso(String idProceso);
}
