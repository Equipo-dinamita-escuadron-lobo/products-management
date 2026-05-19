package com.products_management.copy.application.services;

import com.products_management.copy.application.input.ICleanupProductsCopyPort;
import com.products_management.copy.application.output.ICopyJobLogRepositoryPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Servicio para limpiar los registros de log de un proceso de copia terminado.
 * REQ-CONTRACT-01 (DELETE /{idProceso}/cleanup).
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CleanupProductsCopyService implements ICleanupProductsCopyPort {

    private final ICopyJobLogRepositoryPort logRepo;

    @Override
    public void limpiar(String idProceso) {
        log.info("Limpiando registros de copia de productos para proceso {}", idProceso);
        logRepo.eliminarPorIdProceso(idProceso);
    }
}
