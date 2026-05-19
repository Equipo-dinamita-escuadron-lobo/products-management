package com.products_management.copy.infraestructure.adapters.input.rest.controller;

import com.products_management.copy.application.input.*;
import com.products_management.copy.domain.exceptions.DuplicateCopyJobException;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controlador REST del bounded context copy en products-management.
 * Expone los 4 endpoints del contrato uniforme bajo /api/products/copy.
 * REQ-CONTRACT-01, ADR-30.
 */
@RestController
@RequestMapping("/api/products/copy")
@RequiredArgsConstructor
@Slf4j
public class CopyProductsController {

    private final IExecuteProductsCopyPhasePort executePort;
    private final IGetProductsCopyStatusPort statusPort;
    private final ICancelProductsCopyPort cancelPort;
    private final ICleanupProductsCopyPort cleanupPort;

    /**
     * POST /api/products/copy/phase
     * Ejecuta una fase del proceso de copia de productos.
     * Acepta equivalenciasPrev[] para remapeo de FKs cross-servicio (CATALOGUE).
     */
    @PostMapping("/phase")
    public ResponseEntity<CopyPhaseResponseDto> executePhase(
            @Valid @RequestBody CopyPhaseRequestDto request) {
        log.info("Ejecutando fase {} para proceso {}", request.getFase(), request.getIdProceso());
        CopyPhaseResponseDto response = executePort.ejecutar(request);
        HttpStatus status = resolverHttpStatus(response.getEstado());
        return ResponseEntity.status(status).body(response);
    }

    /**
     * GET /api/products/copy/{idProceso}/status
     * Consulta el estado de un proceso de copia.
     */
    @GetMapping("/{idProceso}/status")
    public ResponseEntity<CopyStatusResponseDto> getStatus(
            @PathVariable String idProceso) {
        CopyStatusResponseDto response = statusPort.obtenerEstado(idProceso);
        return ResponseEntity.ok(response);
    }

    /**
     * POST /api/products/copy/{idProceso}/cancel
     * Cancela un proceso de copia en curso.
     */
    @PostMapping("/{idProceso}/cancel")
    public ResponseEntity<CopyCancelResponseDto> cancel(
            @PathVariable String idProceso) {
        CopyCancelResponseDto response = cancelPort.cancelar(idProceso);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/products/copy/{idProceso}/cleanup
     * Elimina registros temporales de un proceso terminado.
     */
    @DeleteMapping("/{idProceso}/cleanup")
    public ResponseEntity<Void> cleanup(
            @PathVariable String idProceso) {
        cleanupPort.limpiar(idProceso);
        return ResponseEntity.noContent().build();
    }

    // ----------------------------------------------------------------
    // Manejo de excepciones
    // ----------------------------------------------------------------

    @ExceptionHandler(DuplicateCopyJobException.class)
    public ResponseEntity<String> handleNotFound(DuplicateCopyJobException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    private HttpStatus resolverHttpStatus(String estado) {
        if (estado == null) return HttpStatus.INTERNAL_SERVER_ERROR;
        return switch (estado) {
            case "COMPLETADO", "COMPLETADO_CON_ADVERTENCIAS" -> HttpStatus.OK;
            case "ERROR_NO_REINTENTABLE" -> HttpStatus.UNPROCESSABLE_ENTITY;
            case "ERROR_REINTENTABLE" -> HttpStatus.INTERNAL_SERVER_ERROR;
            default -> HttpStatus.OK;
        };
    }
}
