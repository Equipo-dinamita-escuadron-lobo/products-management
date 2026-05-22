package com.products_management.copy.infraestructure.persistence;

import com.products_management.copy.domain.enums.CopyEstado;
import com.products_management.copy.infraestructure.adapters.output.persistence.jpa.CopyJobLogEntity;
import com.products_management.copy.infraestructure.adapters.output.persistence.jpa.CopyJobLogJpaRepository;
import com.products_management.infraestructure.output.multitenancy.utils.TenantContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests RED → GREEN de persistencia para el bounded context copy.
 * Task 3.10 — verifica que copy_job_log persiste correctamente con H2.
 * Usa @SpringBootTest para evitar conflictos con multi-tenancy en @DataJpaTest.
 */
@SpringBootTest
@ActiveProfiles("test")
@Transactional
class CopyJobLogPersistenceTest {

    @Autowired
    private CopyJobLogJpaRepository repository;

    @BeforeEach
    void setUpTenant() {
        // CopyJobLogEntity no tiene @TenantId pero el contexto Hibernate
        // requiere un tenant establecido para abrirse sin error.
        TenantContext.setTenantId("test-tenant");
    }

    @AfterEach
    void clearTenant() {
        TenantContext.clear();
    }

    @Test
    void guardarYBuscarPorIdProcesoYFase() {
        String idProceso = UUID.randomUUID().toString();

        CopyJobLogEntity entity = CopyJobLogEntity.builder()
                .idProceso(idProceso)
                .fase(2)
                .modulo("products")
                .estado(CopyEstado.COMPLETADO)
                .fechaInicio(Instant.now())
                .equivalenciasGeneradas(5)
                .build();

        repository.save(entity);

        Optional<CopyJobLogEntity> encontrado = repository.findByIdProcesoAndFase(idProceso, 2);

        assertThat(encontrado).isPresent();
        assertThat(encontrado.get().getModulo()).isEqualTo("products");
        assertThat(encontrado.get().getEstado()).isEqualTo(CopyEstado.COMPLETADO);
        assertThat(encontrado.get().getEquivalenciasGeneradas()).isEqualTo(5);
    }

    @Test
    void buscarPorIdProcesoRetornaElMasReciente() {
        String idProceso = UUID.randomUUID().toString();

        repository.save(CopyJobLogEntity.builder()
                .idProceso(idProceso)
                .fase(1)
                .modulo("products")
                .estado(CopyEstado.COMPLETADO)
                .fechaInicio(Instant.now().minusSeconds(100))
                .build());

        repository.save(CopyJobLogEntity.builder()
                .idProceso(idProceso)
                .fase(2)
                .modulo("products")
                .estado(CopyEstado.EN_PROCESO)
                .fechaInicio(Instant.now())
                .build());

        Optional<CopyJobLogEntity> reciente = repository
                .findTopByIdProcesoOrderByFechaInicioDesc(idProceso);

        assertThat(reciente).isPresent();
        assertThat(reciente.get().getFase()).isEqualTo(2);
        assertThat(reciente.get().getEstado()).isEqualTo(CopyEstado.EN_PROCESO);
    }

    @Test
    void eliminarPorIdProcesoDebeEliminarTodosLosRegistros() {
        String idProceso = UUID.randomUUID().toString();

        repository.save(CopyJobLogEntity.builder()
                .idProceso(idProceso)
                .fase(1)
                .modulo("products")
                .estado(CopyEstado.COMPLETADO)
                .fechaInicio(Instant.now())
                .build());

        repository.deleteByIdProceso(idProceso);

        assertThat(repository.findByIdProcesoAndFase(idProceso, 1)).isEmpty();
    }

    @Test
    void equivalenciasGeneradasDefaultEsCero() {
        String idProceso = UUID.randomUUID().toString();

        CopyJobLogEntity entity = CopyJobLogEntity.builder()
                .idProceso(idProceso)
                .fase(1)
                .modulo("products")
                .estado(CopyEstado.EN_PROCESO)
                .fechaInicio(Instant.now())
                .build();

        CopyJobLogEntity saved = repository.save(entity);

        assertThat(saved.getEquivalenciasGeneradas()).isEqualTo(0);
    }
}
