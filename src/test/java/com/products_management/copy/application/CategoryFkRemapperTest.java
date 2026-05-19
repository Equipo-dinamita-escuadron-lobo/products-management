package com.products_management.copy.application;

import com.products_management.copy.application.services.CategoryFkRemapper;
import com.products_management.copy.domain.exceptions.MissingEquivalenceException;
import com.products_management.copy.infraestructure.adapters.input.rest.dto.CopyEquivalenciaDto;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests RED → GREEN para CategoryFkRemapper.
 * Task 3.7 — verifica los 5 escenarios de remapeo de FKs cross-servicio.
 */
class CategoryFkRemapperTest {

    private CategoryFkRemapper remapper;

    /** Equivalencias de ejemplo provenientes del CATALOGUE (Fase 1). */
    private List<CopyEquivalenciaDto> equivalenciasPrev;

    @BeforeEach
    void setUp() {
        remapper = new CategoryFkRemapper();

        equivalenciasPrev = List.of(
            CopyEquivalenciaDto.builder().tabla("account").idViejo("100").idNuevo("200").build(),
            CopyEquivalenciaDto.builder().tabla("account").idViejo("101").idNuevo("201").build(),
            CopyEquivalenciaDto.builder().tabla("account").idViejo("102").idNuevo("202").build(),
            CopyEquivalenciaDto.builder().tabla("account").idViejo("103").idNuevo("203").build(),
            CopyEquivalenciaDto.builder().tabla("tax").idViejo("50").idNuevo("150").build()
        );
    }

    @Test
    void remapearInventoryIdDebeUsarEquivalenciaAccount() {
        CategoryEntity categoria = new CategoryEntity();
        categoria.setInventoryId(100L);
        categoria.setCostId(101L);
        categoria.setSaleId(102L);
        categoria.setReturnId(103L);
        categoria.setTaxId(50L);

        List<String> advertencias = new java.util.ArrayList<>();
        remapper.remapear(categoria, equivalenciasPrev, advertencias);

        assertThat(categoria.getInventoryId()).isEqualTo(200L);
        assertThat(categoria.getCostId()).isEqualTo(201L);
        assertThat(categoria.getSaleId()).isEqualTo(202L);
        assertThat(categoria.getReturnId()).isEqualTo(203L);
        assertThat(advertencias).isEmpty();
    }

    @Test
    void remapearTaxIdDebeUsarEquivalenciaTax() {
        CategoryEntity categoria = new CategoryEntity();
        categoria.setTaxId(50L);

        List<String> advertencias = new java.util.ArrayList<>();
        remapper.remapear(categoria, equivalenciasPrev, advertencias);

        assertThat(categoria.getTaxId()).isEqualTo(150L);
    }

    @Test
    void fkOpcionalNullNoDebeGenerarAdvertencia() {
        // inventoryId null es un FK opcional — no se debe advertir ni fallar
        CategoryEntity categoria = new CategoryEntity();
        categoria.setInventoryId(null);
        categoria.setCostId(null);
        categoria.setSaleId(null);
        categoria.setReturnId(null);
        categoria.setTaxId(null);

        List<String> advertencias = new java.util.ArrayList<>();
        remapper.remapear(categoria, equivalenciasPrev, advertencias);

        assertThat(advertencias).isEmpty();
        assertThat(categoria.getInventoryId()).isNull();
    }

    @Test
    void fkAccountSinEquivalenciaDebeRegistrarAdvertenciaYSetearNull() {
        // inventoryId=999 no tiene equivalencia → advertencia y null
        CategoryEntity categoria = new CategoryEntity();
        categoria.setInventoryId(999L);

        List<String> advertencias = new java.util.ArrayList<>();
        remapper.remapear(categoria, equivalenciasPrev, advertencias);

        assertThat(categoria.getInventoryId()).isNull();
        assertThat(advertencias).hasSize(1);
        assertThat(advertencias.get(0)).contains("999");
    }

    @Test
    void equivalenciasPrevVaciaDebeRegistrarAdvertenciasParaTodasLasFKs() {
        CategoryEntity categoria = new CategoryEntity();
        categoria.setInventoryId(100L);
        categoria.setTaxId(50L);

        List<String> advertencias = new java.util.ArrayList<>();
        remapper.remapear(categoria, List.of(), advertencias);

        // Al menos 2 advertencias (inventoryId y taxId)
        assertThat(advertencias).hasSizeGreaterThanOrEqualTo(2);
        assertThat(categoria.getInventoryId()).isNull();
        assertThat(categoria.getTaxId()).isNull();
    }
}
