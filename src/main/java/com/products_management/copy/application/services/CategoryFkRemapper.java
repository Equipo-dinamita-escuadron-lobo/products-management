package com.products_management.copy.application.services;

import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyEquivalenciaDto;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Remapea las FKs cross-servicio de CategoryEntity usando las equivalencias
 * previas recibidas del orquestador (generadas por CATALOGUE en Fase 1).
 *
 * FKs de tipo account: inventoryId, costId, saleId, returnId.
 * FKs de tipo tax: taxId.
 *
 * Si una FK no nula no tiene equivalencia → se registra advertencia y se pone null.
 * Si una FK es null → no se toca y no genera advertencia.
 *
 * REQ-EQUIVPREV-02, ADR-30.
 */
@Component
@Slf4j
public class CategoryFkRemapper {

    /**
     * Remapea todas las FKs cross-servicio de la categoría usando equivalenciasPrev.
     *
     * @param categoria       entidad a remapear (modificada in-place)
     * @param equivalenciasPrev lista de equivalencias del orquestador
     * @param advertencias    lista de advertencias a acumular
     */
    public void remapear(CategoryEntity categoria,
                         List<CopyEquivalenciaDto> equivalenciasPrev,
                         List<String> advertencias) {

        // Construir índice tabla → (idViejo → idNuevo) para búsqueda O(1)
        Map<String, Map<String, Long>> indice = construirIndice(equivalenciasPrev);

        categoria.setInventoryId(
            remapearFk(categoria.getInventoryId(), "account", "inventoryId",
                       indice, advertencias, categoria.getId()));

        categoria.setCostId(
            remapearFk(categoria.getCostId(), "account", "costId",
                       indice, advertencias, categoria.getId()));

        categoria.setSaleId(
            remapearFk(categoria.getSaleId(), "account", "saleId",
                       indice, advertencias, categoria.getId()));

        categoria.setReturnId(
            remapearFk(categoria.getReturnId(), "account", "returnId",
                       indice, advertencias, categoria.getId()));

        categoria.setTaxId(
            remapearFk(categoria.getTaxId(), "tax", "taxId",
                       indice, advertencias, categoria.getId()));
    }

    // ----------------------------------------------------------------
    // Helpers privados
    // ----------------------------------------------------------------

    /**
     * Intenta resolver el nuevo ID para una FK dada.
     * Si la FK es null → devuelve null sin advertencia.
     * Si la FK no está en equivalencias → registra advertencia y devuelve null.
     */
    private Long remapearFk(Long idViejo,
                             String tabla,
                             String campo,
                             Map<String, Map<String, Long>> indice,
                             List<String> advertencias,
                             Long categoriaId) {
        if (idViejo == null) {
            return null;
        }

        Map<String, Long> equivalencias = indice.getOrDefault(tabla, Map.of());
        Long idNuevo = equivalencias.get(String.valueOf(idViejo));

        if (idNuevo == null) {
            String advertencia = String.format(
                "Categoría id=%s campo '%s': FK tabla='%s' idViejo='%s' sin equivalencia; se inserta null.",
                categoriaId, campo, tabla, idViejo
            );
            log.warn(advertencia);
            advertencias.add(advertencia);
            return null;
        }

        return idNuevo;
    }

    /**
     * Construye un índice tabla → (idViejo → idNuevo) desde la lista plana de equivalencias.
     */
    private Map<String, Map<String, Long>> construirIndice(List<CopyEquivalenciaDto> equivalenciasPrev) {
        Map<String, Map<String, Long>> indice = new HashMap<>();

        if (equivalenciasPrev == null) {
            return indice;
        }

        for (CopyEquivalenciaDto eq : equivalenciasPrev) {
            if (eq.getTabla() != null && eq.getIdViejo() != null && eq.getIdNuevo() != null) {
                indice
                    .computeIfAbsent(eq.getTabla(), k -> new HashMap<>())
                    .put(eq.getIdViejo(), Long.parseLong(eq.getIdNuevo()));
            }
        }

        return indice;
    }
}
