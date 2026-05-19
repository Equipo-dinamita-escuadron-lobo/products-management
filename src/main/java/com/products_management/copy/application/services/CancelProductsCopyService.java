package com.products_management.copy.application.services;

import com.products_management.copy.application.input.ICancelProductsCopyPort;
import com.products_management.copy.application.output.ICopyJobLogRepositoryPort;
import com.products_management.copy.domain.enums.CopyEstado;
import com.products_management.copy.domain.exceptions.DuplicateCopyJobException;
import com.products_management.copy.domain.models.CopyJobLog;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyCancelResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;

/**
 * Servicio para cancelar un proceso de copia de productos en curso.
 * REQ-CONTRACT-01 (POST /{idProceso}/cancel).
 */
@Service
@RequiredArgsConstructor
public class CancelProductsCopyService implements ICancelProductsCopyPort {

    private final ICopyJobLogRepositoryPort logRepo;

    @Override
    public CopyCancelResponseDto cancelar(String idProceso) {
        CopyJobLog log = logRepo.buscarPorIdProceso(idProceso)
                .orElseThrow(() -> new DuplicateCopyJobException(idProceso, 0));

        CopyJobLog cancelado = CopyJobLog.builder()
                .idProceso(log.getIdProceso())
                .fase(log.getFase())
                .modulo(log.getModulo())
                .estado(CopyEstado.CANCELADO)
                .fechaInicio(log.getFechaInicio())
                .fechaFin(Instant.now())
                .equivalenciasGeneradas(log.getEquivalenciasGeneradas())
                .build();
        logRepo.guardar(cancelado);

        return CopyCancelResponseDto.builder()
                .estado(CopyEstado.CANCELADO.name())
                .mensaje("Proceso de copia cancelado exitosamente")
                .build();
    }
}
