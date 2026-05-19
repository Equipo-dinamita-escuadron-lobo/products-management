package com.products_management.copy.application;

import com.products_management.copy.application.services.CopyEquivalenceMapper;
import com.products_management.copy.domain.models.CopyEquivalencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para CopyEquivalenceMapper.
 * Verifica registrar, resolver, limpiar y toList.
 */
class CopyEquivalenceMapperTest {

    private CopyEquivalenceMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new CopyEquivalenceMapper();
    }

    @Test
    void registrarYResolverNuevoId() {
        mapper.registrar("category", 1L, 100L);

        assertThat(mapper.resolverNuevoId("category", 1L)).isEqualTo(100L);
    }

    @Test
    void resolverNuevoIdSinRegistroRetornaNull() {
        assertThat(mapper.resolverNuevoId("category", 999L)).isNull();
    }

    @Test
    void limpiarEliminaTodasLasEquivalencias() {
        mapper.registrar("category", 1L, 100L);
        mapper.limpiar();

        assertThat(mapper.resolverNuevoId("category", 1L)).isNull();
    }

    @Test
    void toListRetornaTodasLasEquivalencias() {
        mapper.registrar("unit_of_measure", 1L, 10L);
        mapper.registrar("category", 2L, 20L);

        List<CopyEquivalencia> lista = mapper.toList();

        assertThat(lista).hasSize(2);
        assertThat(lista).anyMatch(e -> e.getTabla().equals("unit_of_measure")
                && e.getIdViejo().equals("1") && e.getIdNuevo().equals("10"));
        assertThat(lista).anyMatch(e -> e.getTabla().equals("category")
                && e.getIdViejo().equals("2") && e.getIdNuevo().equals("20"));
    }

    @Test
    void toListVacioRetornaListaVacia() {
        assertThat(mapper.toList()).isEmpty();
    }

    @Test
    void equivalenciasDiferentesTablasMismoId() {
        mapper.registrar("category", 1L, 100L);
        mapper.registrar("unit_of_measure", 1L, 200L);

        assertThat(mapper.resolverNuevoId("category", 1L)).isEqualTo(100L);
        assertThat(mapper.resolverNuevoId("unit_of_measure", 1L)).isEqualTo(200L);
    }
}
