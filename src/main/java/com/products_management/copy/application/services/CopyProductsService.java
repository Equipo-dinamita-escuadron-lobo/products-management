package com.products_management.copy.application.services;

import com.products_management.copy.application.input.IExecuteProductsCopyPhasePort;
import com.products_management.copy.application.output.*;
import com.products_management.copy.domain.enums.CopyEstado;
import com.products_management.copy.domain.models.CopyJobLog;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyEquivalenciaDto;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyPhaseRequestDto;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyPhaseResponseDto;
import com.products_management.infraestructure.output.persistence.entity.*;
import com.products_management.infraestructure.output.persistence.multitenancy.utils.TenantContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Servicio de aplicación que orquesta la ejecución de una fase de copia de productos.
 *
 * Orden secuencial: UnitOfMeasure → ProductType → Category → Product.
 * Category usa remapeo de FKs cross-servicio (CategoryFkRemapper).
 * Product usa remapeo de FKs internas (uom, category, productType).
 * Idempotencia via copy_job_log lookup por (idProceso, fase).
 * Tenant override programático: try { TenantContext.setTenantId(entDestino) } finally { clear() }.
 *
 * REQ-PRODUCTS-04, REQ-PRODUCTS-05, REQ-EQUIVPREV-02, ADR-30, ADR-33.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class CopyProductsService implements IExecuteProductsCopyPhasePort {

    /** Nombre del módulo para log de idempotencia. */
    private static final String MODULO = "products";

    private final ICopyJobLogRepositoryPort logRepo;
    private final IUnitOfMeasureSourceRepositoryPort uomSource;
    private final IUnitOfMeasureTargetRepositoryPort uomTarget;
    private final IProductTypeSourceRepositoryPort ptSource;
    private final IProductTypeTargetRepositoryPort ptTarget;
    private final ICategorySourceRepositoryPort catSource;
    private final ICategoryTargetRepositoryPort catTarget;
    private final IProductSourceRepositoryPort prodSource;
    private final IProductTargetRepositoryPort prodTarget;
    private final CopyEquivalenceMapper equivalenceMapper;
    private final CategoryFkRemapper categoryFkRemapper;

    @Override
    public CopyPhaseResponseDto ejecutar(CopyPhaseRequestDto request) {
        // Validación básica
        if (request.getEntOrigen().equals(request.getEntDestino())) {
            return CopyPhaseResponseDto.builder()
                    .estado("ERROR_NO_REINTENTABLE")
                    .mensaje("entOrigen y entDestino no pueden ser iguales")
                    .equivalenciasGeneradas(Collections.emptyList())
                    .advertencias(Collections.emptyList())
                    .build();
        }

        String idProceso = request.getIdProceso().toString();

        // Idempotencia: retornar resultado previo si la fase ya fue ejecutada
        Optional<CopyJobLog> previo = logRepo.buscarPorIdProcesoYFase(idProceso, request.getFase());
        if (previo.isPresent()) {
            log.info("Fase {} del proceso {} ya fue ejecutada — retornando resultado previo (idempotencia)",
                     request.getFase(), idProceso);
            return construirResponseDesdeLog(previo.get());
        }

        // Registrar inicio
        CopyJobLog logInicio = CopyJobLog.builder()
                .idProceso(request.getIdProceso())
                .fase(request.getFase())
                .modulo(MODULO)
                .estado(CopyEstado.EN_PROCESO)
                .fechaInicio(Instant.now())
                .equivalenciasGeneradas(0)
                .build();
        logRepo.guardar(logInicio);

        // Limpiar mapper para esta ejecución
        equivalenceMapper.limpiar();

        List<String> advertencias = new ArrayList<>();

        // ----------------------------------------------------------------
        // Tenant override: forzar entDestino durante toda la ejecución.
        // UnitOfMeasure y ProductType no tienen @TenantId — se setean manualmente.
        // Category y Product tienen @TenantId — Hibernate lo aplica automáticamente.
        // ----------------------------------------------------------------
        String tenantOriginal = TenantContext.getTenantId();
        TenantContext.setTenantId(request.getEntDestino());

        int totalRegistros = 0;

        try {
            // Orden secuencial: UnitOfMeasure → ProductType → Category → Product
            totalRegistros += copiarUnidadesDeMedida(request, advertencias);
            totalRegistros += copiarTiposDeProducto(request, advertencias);
            totalRegistros += copiarCategorias(request, advertencias);
            totalRegistros += copiarProductos(request, advertencias);

        } catch (Exception e) {
            log.error("Error inesperado durante copia del proceso {}: {}", idProceso, e.getMessage(), e);
            registrarFallo(request, e.getMessage(), logInicio.getFechaInicio());
            return CopyPhaseResponseDto.builder()
                    .estado("ERROR_REINTENTABLE")
                    .mensaje("Error interno: " + e.getMessage())
                    .equivalenciasGeneradas(Collections.emptyList())
                    .advertencias(Collections.emptyList())
                    .build();
        } finally {
            // Restaurar tenant original para evitar leaks en el pool de hilos
            if (tenantOriginal != null) {
                TenantContext.setTenantId(tenantOriginal);
            } else {
                TenantContext.clear();
            }
        }

        // Construir respuesta final
        CopyEstado estadoFinal = advertencias.isEmpty()
                ? CopyEstado.COMPLETADO
                : CopyEstado.COMPLETADO_CON_ADVERTENCIAS;

        List<CopyEquivalenciaDto> equivalencias = equivalenceMapper.toList().stream()
                .map(eq -> CopyEquivalenciaDto.builder()
                        .tabla(eq.getTabla())
                        .idViejo(eq.getIdViejo())
                        .idNuevo(eq.getIdNuevo())
                        .build())
                .collect(Collectors.toList());

        // Guardar log final
        CopyJobLog logFin = CopyJobLog.builder()
                .idProceso(request.getIdProceso())
                .fase(request.getFase())
                .modulo(MODULO)
                .estado(estadoFinal)
                .fechaInicio(logInicio.getFechaInicio())
                .fechaFin(Instant.now())
                .equivalenciasGeneradas(equivalencias.size())
                .build();
        logRepo.guardar(logFin);

        return CopyPhaseResponseDto.builder()
                .estado(estadoFinal.name())
                .registrosProcesados(totalRegistros)
                .equivalenciasGeneradas(equivalencias)
                .mensaje("Copia completada exitosamente")
                .advertencias(advertencias)
                .build();
    }

    // ----------------------------------------------------------------
    // Copiar UnitOfMeasure (sin @TenantId — enterpriseId manual)
    // ----------------------------------------------------------------
    private int copiarUnidadesDeMedida(CopyPhaseRequestDto request, List<String> advertencias) {
        List<UnitOfMeasureEntity> origenList = uomSource
                .findByEntOrigenBeforeSnapshot(request.getEntOrigen(), request.getSnapshotCorte());

        int copiadas = 0;
        for (UnitOfMeasureEntity original : origenList) {
            UnitOfMeasureEntity nueva = new UnitOfMeasureEntity();
            nueva.setId(null);
            nueva.setName(original.getName());
            nueva.setDescription(original.getDescription());
            nueva.setAbbreviation(original.getAbbreviation());
            nueva.setState(original.getState());
            // enterpriseId manual — no tiene @TenantId
            nueva.setEnterpriseId(request.getEntDestino());

            UnitOfMeasureEntity guardada = uomTarget.guardar(nueva);
            equivalenceMapper.registrar("unit_of_measure", original.getId(), guardada.getId());
            copiadas++;
        }
        return copiadas;
    }

    // ----------------------------------------------------------------
    // Copiar ProductType (sin @TenantId — enterpriseId manual)
    // ----------------------------------------------------------------
    private int copiarTiposDeProducto(CopyPhaseRequestDto request, List<String> advertencias) {
        List<ProductTypeEntity> origenList = ptSource
                .findByEntOrigenBeforeSnapshot(request.getEntOrigen(), request.getSnapshotCorte());

        int copiados = 0;
        for (ProductTypeEntity original : origenList) {
            ProductTypeEntity nuevo = ProductTypeEntity.builder()
                    .id(null)
                    .name(original.getName())
                    .description(original.getDescription())
                    // enterpriseId manual — no tiene @TenantId
                    .enterpriseId(request.getEntDestino())
                    .build();

            ProductTypeEntity guardado = ptTarget.guardar(nuevo);
            equivalenceMapper.registrar("product_type", original.getId(), guardado.getId());
            copiados++;
        }
        return copiados;
    }

    // ----------------------------------------------------------------
    // Copiar Category (con @TenantId Hibernate + remapeo FKs cross-servicio)
    // ----------------------------------------------------------------
    private int copiarCategorias(CopyPhaseRequestDto request, List<String> advertencias) {
        List<CategoryEntity> origenList = catSource
                .findByEntOrigenBeforeSnapshot(request.getEntOrigen(), request.getSnapshotCorte());

        int copiadas = 0;
        List<CopyEquivalenciaDto> equivPrev = request.getEquivalenciasPrev() != null
                ? request.getEquivalenciasPrev()
                : Collections.emptyList();

        for (CategoryEntity original : origenList) {
            CategoryEntity nueva = new CategoryEntity();
            nueva.setId(null);
            nueva.setName(original.getName());
            nueva.setDescription(original.getDescription());
            nueva.setState(original.getState());
            // enterpriseId también se setea manualmente (además del @TenantId de Hibernate)
            nueva.setEnterpriseId(request.getEntDestino());
            // Copiar FKs antes del remap para que el remapper las vea
            nueva.setInventoryId(original.getInventoryId());
            nueva.setCostId(original.getCostId());
            nueva.setSaleId(original.getSaleId());
            nueva.setReturnId(original.getReturnId());
            nueva.setTaxId(original.getTaxId());

            // Remapear FKs cross-servicio (account, tax) con equivalenciasPrev
            categoryFkRemapper.remapear(nueva, equivPrev, advertencias);

            CategoryEntity guardada = catTarget.guardar(nueva);
            equivalenceMapper.registrar("category", original.getId(), guardada.getId());
            copiadas++;
        }
        return copiadas;
    }

    // ----------------------------------------------------------------
    // Copiar Product (con @TenantId Hibernate + remapeo FKs internas)
    // ----------------------------------------------------------------
    private int copiarProductos(CopyPhaseRequestDto request, List<String> advertencias) {
        List<ProductEntity> origenList = prodSource
                .findByEntOrigenBeforeSnapshot(request.getEntOrigen(), request.getSnapshotCorte());

        int copiados = 0;
        for (ProductEntity original : origenList) {
            ProductEntity nuevo = new ProductEntity();
            nuevo.setId(null);
            nuevo.setCode(original.getCode());
            nuevo.setItemType(original.getItemType());
            nuevo.setDescription(original.getDescription());
            nuevo.setQuantity(original.getQuantity());
            nuevo.setTaxPercentage(original.getTaxPercentage());
            nuevo.setCreationDate(original.getCreationDate());
            nuevo.setCost(original.getCost());
            nuevo.setState(original.getState());
            nuevo.setReference(original.getReference());
            // enterpriseId manual
            nuevo.setEnterpriseId(request.getEntDestino());

            // Remapear FKs internas (unitOfMeasureId, categoryId, productTypeId)
            nuevo.setUnitOfMeasureId(
                remapearFkInterna(original.getUnitOfMeasureId(), "unit_of_measure", advertencias, original.getId()));
            nuevo.setCategoryId(
                remapearFkInterna(original.getCategoryId(), "category", advertencias, original.getId()));
            nuevo.setProductTypeId(
                remapearFkInterna(original.getProductTypeId(), "product_type", advertencias, original.getId()));

            ProductEntity guardado = prodTarget.guardar(nuevo);
            equivalenceMapper.registrar("product", original.getId(), guardado.getId());
            copiados++;
        }
        return copiados;
    }

    // ----------------------------------------------------------------
    // Helpers
    // ----------------------------------------------------------------

    /**
     * Remapea una FK interna usando el equivalenceMapper.
     * Si no hay equivalencia → registra advertencia y retorna null.
     */
    private Long remapearFkInterna(Long idViejo, String tabla,
                                    List<String> advertencias, Long productoId) {
        if (idViejo == null) {
            return null;
        }
        Long idNuevo = equivalenceMapper.resolverNuevoId(tabla, idViejo);
        if (idNuevo == null) {
            String adv = String.format(
                "Producto id=%s FK tabla='%s' idViejo='%s' sin equivalencia interna; se inserta null.",
                productoId, tabla, idViejo
            );
            log.warn(adv);
            advertencias.add(adv);
        }
        return idNuevo;
    }

    private CopyPhaseResponseDto construirResponseDesdeLog(CopyJobLog log) {
        return CopyPhaseResponseDto.builder()
                .estado(log.getEstado().name())
                .registrosProcesados(log.getEquivalenciasGeneradas() != null ? log.getEquivalenciasGeneradas() : 0)
                .equivalenciasGeneradas(Collections.emptyList())
                .mensaje("Resultado de ejecución previa (idempotencia)")
                .advertencias(Collections.emptyList())
                .build();
    }

    private void registrarFallo(CopyPhaseRequestDto request, String mensaje, Instant fechaInicio) {
        try {
            CopyJobLog logFallo = CopyJobLog.builder()
                    .idProceso(request.getIdProceso())
                    .fase(request.getFase())
                    .modulo(MODULO)
                    .estado(CopyEstado.FALLIDO)
                    .fechaInicio(fechaInicio != null ? fechaInicio : Instant.now())
                    .fechaFin(Instant.now())
                    .equivalenciasGeneradas(0)
                    .errorMessage(mensaje)
                    .build();
            logRepo.guardar(logFallo);
        } catch (Exception e) {
            log.error("Error al registrar fallo de copia: {}", e.getMessage());
        }
    }
}
