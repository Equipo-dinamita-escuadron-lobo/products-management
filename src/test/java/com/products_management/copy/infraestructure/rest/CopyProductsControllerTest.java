package com.products_management.copy.infraestructure.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.products_management.copy.application.input.*;
import com.products_management.copy.infraestructure.adapters.input.rest.controller.CopyProductsController;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.*;
import com.products_management.infraestructure.output.multitenancy.interceptor.TenantInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Tests RED → GREEN para CopyProductsController con @WebMvcTest.
 * Task 3.12 — cubre happy path, idempotencia y respuestas HTTP correctas.
 * Nota: seguridad deshabilitada en WebMvcTest — los tests de Bearer se validan en E2E.
 */
@WebMvcTest(controllers = CopyProductsController.class)
@AutoConfigureMockMvc(addFilters = false)
class CopyProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private IExecuteProductsCopyPhasePort executePort;

    @MockBean
    private IGetProductsCopyStatusPort statusPort;

    @MockBean
    private ICancelProductsCopyPort cancelPort;

    @MockBean
    private ICleanupProductsCopyPort cleanupPort;

    /** Necesario porque WebConfiguration inyecta TenantInterceptor vía constructor. */
    @MockBean
    private TenantInterceptor tenantInterceptor;

    // -----------------------------------------------------------------
    // POST /phase — happy path
    // -----------------------------------------------------------------

    @Test
    void postPhaseHappyPathDebeRetornar200() throws Exception {
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(UUID.randomUUID())
                .fase(2)
                .entOrigen("emp-A")
                .entDestino("emp-B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(Collections.emptyList())
                .build();

        CopyPhaseResponseDto response = CopyPhaseResponseDto.builder()
                .estado("COMPLETADO")
                .registrosProcesados(5)
                .equivalenciasGeneradas(List.of())
                .mensaje("Copia completada exitosamente")
                .advertencias(List.of())
                .build();

        when(executePort.ejecutar(any())).thenReturn(response);

        mockMvc.perform(post("/api/products/copy/phase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("COMPLETADO"))
                .andExpect(jsonPath("$.registrosProcesados").value(5));
    }

    @Test
    void postPhaseIdempotenciaDebeRetornar200ConEstadoPrevio() throws Exception {
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(UUID.randomUUID())
                .fase(2)
                .entOrigen("emp-A")
                .entDestino("emp-B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(Collections.emptyList())
                .build();

        CopyPhaseResponseDto responseIdempotente = CopyPhaseResponseDto.builder()
                .estado("COMPLETADO")
                .registrosProcesados(3)
                .equivalenciasGeneradas(List.of())
                .mensaje("Resultado de ejecución previa (idempotencia)")
                .advertencias(List.of())
                .build();

        when(executePort.ejecutar(any())).thenReturn(responseIdempotente);

        mockMvc.perform(post("/api/products/copy/phase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Resultado de ejecución previa (idempotencia)"));
    }

    @Test
    void postPhaseConErrorNoReintentableDebeRetornar422() throws Exception {
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(UUID.randomUUID())
                .fase(2)
                .entOrigen("emp-A")
                .entDestino("emp-A") // igual = error
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(Collections.emptyList())
                .build();

        CopyPhaseResponseDto errorResponse = CopyPhaseResponseDto.builder()
                .estado("ERROR_NO_REINTENTABLE")
                .mensaje("entOrigen y entDestino no pueden ser iguales")
                .equivalenciasGeneradas(List.of())
                .advertencias(List.of())
                .build();

        when(executePort.ejecutar(any())).thenReturn(errorResponse);

        mockMvc.perform(post("/api/products/copy/phase")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.estado").value("ERROR_NO_REINTENTABLE"));
    }

    // -----------------------------------------------------------------
    // GET /status
    // -----------------------------------------------------------------

    @Test
    void getStatusDebeRetornar200() throws Exception {
        String idProceso = UUID.randomUUID().toString();

        when(statusPort.obtenerEstado(anyString())).thenReturn(
            CopyStatusResponseDto.builder()
                    .fase(2)
                    .estado("COMPLETADO")
                    .registrosProcesados(5)
                    .intentos(1)
                    .build());

        mockMvc.perform(get("/api/products/copy/{idProceso}/status", idProceso))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("COMPLETADO"))
                .andExpect(jsonPath("$.fase").value(2));
    }

    // -----------------------------------------------------------------
    // POST /cancel
    // -----------------------------------------------------------------

    @Test
    void postCancelDebeRetornar200() throws Exception {
        String idProceso = UUID.randomUUID().toString();

        when(cancelPort.cancelar(anyString())).thenReturn(
            CopyCancelResponseDto.builder()
                    .estado("CANCELADO")
                    .mensaje("Proceso cancelado")
                    .build());

        mockMvc.perform(post("/api/products/copy/{idProceso}/cancel", idProceso))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("CANCELADO"));
    }

    // -----------------------------------------------------------------
    // DELETE /cleanup
    // -----------------------------------------------------------------

    @Test
    void deleteCleanupDebeRetornar204() throws Exception {
        String idProceso = UUID.randomUUID().toString();

        mockMvc.perform(delete("/api/products/copy/{idProceso}/cleanup", idProceso))
                .andExpect(status().isNoContent());
    }
}
