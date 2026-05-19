package com.products_management.copy.domain;

import com.products_management.copy.domain.enums.CopyEstado;
import com.products_management.copy.domain.models.CopyJobLog;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests RED para el modelo de dominio CopyJobLog.
 * Task 3.2 — verifica builder y valores por defecto.
 */
class CopyJobLogTest {

    @Test
    void builderDebeCrearLogConCamposBasicos() {
        UUID idProceso = UUID.randomUUID();
        Instant ahora = Instant.now();

        CopyJobLog log = CopyJobLog.builder()
                .idProceso(idProceso)
                .fase(2)
                .modulo("products")
                .estado(CopyEstado.EN_PROCESO)
                .fechaInicio(ahora)
                .build();

        assertThat(log.getIdProceso()).isEqualTo(idProceso);
        assertThat(log.getFase()).isEqualTo(2);
        assertThat(log.getModulo()).isEqualTo("products");
        assertThat(log.getEstado()).isEqualTo(CopyEstado.EN_PROCESO);
        assertThat(log.getFechaInicio()).isEqualTo(ahora);
        assertThat(log.getFechaFin()).isNull();
    }

    @Test
    void equivalenciasGeneradasPorDefectoEsCero() {
        CopyJobLog log = CopyJobLog.builder()
                .idProceso(UUID.randomUUID())
                .fase(1)
                .modulo("products")
                .estado(CopyEstado.COMPLETADO)
                .build();

        assertThat(log.getEquivalenciasGeneradas()).isEqualTo(0);
    }

    @Test
    void estadoCompletadoConAdvertenciasEsDistintoDeCompletado() {
        CopyJobLog log = CopyJobLog.builder()
                .estado(CopyEstado.COMPLETADO_CON_ADVERTENCIAS)
                .build();

        assertThat(log.getEstado()).isNotEqualTo(CopyEstado.COMPLETADO);
    }
}
