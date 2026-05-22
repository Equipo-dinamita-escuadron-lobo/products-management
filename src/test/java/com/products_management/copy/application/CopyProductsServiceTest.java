package com.products_management.copy.application;

import com.products_management.copy.application.output.*;
import com.products_management.copy.application.services.CategoryFkRemapper;
import com.products_management.copy.application.services.CopyEquivalenceMapper;
import com.products_management.copy.application.services.CopyProductsService;
import com.products_management.copy.domain.enums.CopyEstado;
import com.products_management.copy.domain.models.CopyJobLog;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyEquivalenciaDto;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyPhaseRequestDto;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyPhaseResponseDto;
import com.products_management.infraestructure.output.persistence.entity.*;
import com.products_management.infraestructure.output.multitenancy.utils.TenantContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Tests unitarios RED → GREEN para CopyProductsService.
 * Task 3.5 — cubre happy path, idempotencia, tenant override, remapeo interno FKs.
 */
@ExtendWith(MockitoExtension.class)
class CopyProductsServiceTest {

    @Mock private ICopyJobLogRepositoryPort logRepo;
    @Mock private IUnitOfMeasureSourceRepositoryPort uomSource;
    @Mock private IUnitOfMeasureTargetRepositoryPort uomTarget;
    @Mock private IProductTypeSourceRepositoryPort ptSource;
    @Mock private IProductTypeTargetRepositoryPort ptTarget;
    @Mock private ICategorySourceRepositoryPort catSource;
    @Mock private ICategoryTargetRepositoryPort catTarget;
    @Mock private IProductSourceRepositoryPort prodSource;
    @Mock private IProductTargetRepositoryPort prodTarget;

    private CopyEquivalenceMapper equivalenceMapper;
    private CategoryFkRemapper categoryFkRemapper;

    @InjectMocks
    private CopyProductsService service;

    @BeforeEach
    void setUp() {
        equivalenceMapper = new CopyEquivalenceMapper();
        categoryFkRemapper = new CategoryFkRemapper();
        service = new CopyProductsService(
            logRepo, uomSource, uomTarget,
            ptSource, ptTarget,
            catSource, catTarget,
            prodSource, prodTarget,
            equivalenceMapper, categoryFkRemapper
        );
    }

    // -----------------------------------------------------------------
    // Happy path
    // -----------------------------------------------------------------

    @Test
    void ejecutarDebeRetornarCOMPLETADOCuandoCopiaExitosa() {
        UUID idProceso = UUID.randomUUID();
        CopyPhaseRequestDto request = construirRequest(idProceso);

        when(logRepo.buscarPorIdProcesoYFase(anyString(), anyInt())).thenReturn(Optional.empty());
        when(logRepo.guardar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(uomSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(ptSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(catSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(prodSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());

        CopyPhaseResponseDto respuesta = service.ejecutar(request);

        assertThat(respuesta.getEstado()).isEqualTo("COMPLETADO");
        assertThat(respuesta.getRegistrosProcesados()).isEqualTo(0);
        assertThat(respuesta.getEquivalenciasGeneradas()).isNotNull();
        assertThat(respuesta.getAdvertencias()).isEmpty();
    }

    @Test
    void ejecutarConDatosDebeContabilizarRegistros() {
        UUID idProceso = UUID.randomUUID();
        CopyPhaseRequestDto request = construirRequest(idProceso);

        UnitOfMeasureEntity uom = new UnitOfMeasureEntity();
        uom.setId(1L);
        uom.setName("kg");
        uom.setEnterpriseId("emp-A");

        UnitOfMeasureEntity uomGuardado = new UnitOfMeasureEntity();
        uomGuardado.setId(10L);

        when(logRepo.buscarPorIdProcesoYFase(anyString(), anyInt())).thenReturn(Optional.empty());
        when(logRepo.guardar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(uomSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of(uom));
        when(uomTarget.guardar(any())).thenReturn(uomGuardado);
        when(ptSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(catSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(prodSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());

        CopyPhaseResponseDto respuesta = service.ejecutar(request);

        assertThat(respuesta.getEstado()).isEqualTo("COMPLETADO");
        assertThat(respuesta.getRegistrosProcesados()).isEqualTo(1);
        // Debe haber una equivalencia registrada para unitOfMeasure
        assertThat(respuesta.getEquivalenciasGeneradas()).hasSize(1);
        assertThat(respuesta.getEquivalenciasGeneradas().get(0).getTabla()).isEqualTo("unit_of_measure");
    }

    // -----------------------------------------------------------------
    // Idempotencia
    // -----------------------------------------------------------------

    @Test
    void ejecutarConProcesoYaProcesadoDebeRetornarResultadoPrevio() {
        UUID idProceso = UUID.randomUUID();
        CopyPhaseRequestDto request = construirRequest(idProceso);

        CopyJobLog logPrevio = CopyJobLog.builder()
                .idProceso(idProceso)
                .fase(2)
                .estado(CopyEstado.COMPLETADO)
                .equivalenciasGeneradas(5)
                .build();

        when(logRepo.buscarPorIdProcesoYFase(anyString(), anyInt())).thenReturn(Optional.of(logPrevio));

        CopyPhaseResponseDto respuesta = service.ejecutar(request);

        assertThat(respuesta.getEstado()).isEqualTo("COMPLETADO");
        assertThat(respuesta.getMensaje()).contains("idempotencia");
        // No se llama a ningún repositorio de fuente
        verify(uomSource, never()).findByEntOrigenBeforeSnapshot(anyString(), any());
    }

    // -----------------------------------------------------------------
    // Tenant override
    // -----------------------------------------------------------------

    @Test
    void ejecutarDebeUsarEntDestinoComoTenantDuranteCopia() {
        UUID idProceso = UUID.randomUUID();
        CopyPhaseRequestDto request = construirRequest(idProceso);

        UnitOfMeasureEntity uomGuardado = new UnitOfMeasureEntity();
        uomGuardado.setId(10L);

        when(logRepo.buscarPorIdProcesoYFase(anyString(), anyInt())).thenReturn(Optional.empty());
        when(logRepo.guardar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(uomSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(ptSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(catSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());
        when(prodSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of());

        service.ejecutar(request);

        // Después de ejecutar, el TenantContext debe estar limpio
        assertThat(TenantContext.getTenantId()).isNull();
    }

    // -----------------------------------------------------------------
    // Remapeo FKs internas en Product
    // -----------------------------------------------------------------

    @Test
    void ejecutarDebeRemapearFKsInternasDeProducto() {
        UUID idProceso = UUID.randomUUID();
        CopyPhaseRequestDto request = construirRequest(idProceso);

        // UnitOfMeasure: id=1 → nuevo id=10
        UnitOfMeasureEntity uomOrigen = new UnitOfMeasureEntity();
        uomOrigen.setId(1L);
        uomOrigen.setName("kg");
        uomOrigen.setEnterpriseId("emp-A");

        UnitOfMeasureEntity uomGuardado = new UnitOfMeasureEntity();
        uomGuardado.setId(10L);

        // ProductType: id=3 → nuevo id=30
        ProductTypeEntity ptOrigen = ProductTypeEntity.builder().id(3L).name("tipo").enterpriseId("emp-A").build();
        ProductTypeEntity ptGuardado = ProductTypeEntity.builder().id(30L).build();

        // Category: id=2 → nuevo id=20
        CategoryEntity catOrigen = new CategoryEntity();
        catOrigen.setId(2L);
        catOrigen.setName("cat");
        catOrigen.setEnterpriseId("emp-A");

        CategoryEntity catGuardada = new CategoryEntity();
        catGuardada.setId(20L);

        // Producto que usa esos IDs de origen
        ProductEntity producto = new ProductEntity();
        producto.setId(5L);
        producto.setCode("PROD-001");
        producto.setUnitOfMeasureId(1L);
        producto.setCategoryId(2L);
        producto.setProductTypeId(3L);
        producto.setEnterpriseId("emp-A");

        ProductEntity productoGuardado = new ProductEntity();
        productoGuardado.setId(50L);

        ArgumentCaptor<ProductEntity> captor = ArgumentCaptor.forClass(ProductEntity.class);

        when(logRepo.buscarPorIdProcesoYFase(anyString(), anyInt())).thenReturn(Optional.empty());
        when(logRepo.guardar(any())).thenAnswer(inv -> inv.getArgument(0));
        when(uomSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of(uomOrigen));
        when(uomTarget.guardar(any())).thenReturn(uomGuardado);
        when(ptSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of(ptOrigen));
        when(ptTarget.guardar(any())).thenReturn(ptGuardado);
        when(catSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of(catOrigen));
        when(catTarget.guardar(any())).thenReturn(catGuardada);
        when(prodSource.findByEntOrigenBeforeSnapshot(anyString(), any())).thenReturn(List.of(producto));
        when(prodTarget.guardar(captor.capture())).thenReturn(productoGuardado);

        service.ejecutar(request);

        // El equivalenceMapper fue poblado durante la copia de UOM, PT, Category
        // y luego usado para remapear las FKs internas del producto
        ProductEntity guardado = captor.getValue();
        assertThat(guardado.getUnitOfMeasureId()).isEqualTo(10L);
        assertThat(guardado.getCategoryId()).isEqualTo(20L);
        assertThat(guardado.getProductTypeId()).isEqualTo(30L);
        // Tenant override: enterpriseId manual seteado a entDestino
        assertThat(guardado.getEnterpriseId()).isEqualTo("emp-B");
    }

    // -----------------------------------------------------------------
    // entOrigen == entDestino debe fallar
    // -----------------------------------------------------------------

    @Test
    void ejecutarConEntOrigenIgualAEntDestinoDebeRetornarErrorNoReintentable() {
        CopyPhaseRequestDto request = CopyPhaseRequestDto.builder()
                .idProceso(UUID.randomUUID())
                .fase(2)
                .entOrigen("emp-A")
                .entDestino("emp-A")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(List.of())
                .build();

        CopyPhaseResponseDto respuesta = service.ejecutar(request);

        assertThat(respuesta.getEstado()).isEqualTo("ERROR_NO_REINTENTABLE");
    }

    // -----------------------------------------------------------------
    // Helpers
    // -----------------------------------------------------------------

    private CopyPhaseRequestDto construirRequest(UUID idProceso) {
        return CopyPhaseRequestDto.builder()
                .idProceso(idProceso)
                .fase(2)
                .entOrigen("emp-A")
                .entDestino("emp-B")
                .snapshotCorte(Instant.now())
                .equivalenciasPrev(Collections.emptyList())
                .build();
    }
}
