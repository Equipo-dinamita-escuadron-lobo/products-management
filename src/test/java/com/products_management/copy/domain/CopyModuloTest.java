package com.products_management.copy.domain;

import com.products_management.copy.domain.enums.CopyEstado;
import com.products_management.copy.domain.enums.CopyModulo;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para los enums del bounded context copy.
 */
class CopyModuloTest {

    @Test
    void copyModuloDebeContenerPRODUCTS() {
        assertThat(CopyModulo.PRODUCTS).isNotNull();
    }

    @Test
    void copyEstadoDebeContenerTodosLosEstados() {
        assertThat(CopyEstado.values()).contains(
            CopyEstado.EN_PROCESO,
            CopyEstado.COMPLETADO,
            CopyEstado.COMPLETADO_CON_ADVERTENCIAS,
            CopyEstado.FALLIDO,
            CopyEstado.CANCELADO,
            CopyEstado.ERROR_NO_REINTENTABLE
        );
    }

    @Test
    void copyModuloDebeContenerTodosLosModulos() {
        assertThat(CopyModulo.values()).contains(
            CopyModulo.CATALOGUE,
            CopyModulo.PRODUCTS,
            CopyModulo.THIRDS,
            CopyModulo.ENTERPRISES
        );
    }
}
