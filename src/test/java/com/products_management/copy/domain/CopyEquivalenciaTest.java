package com.products_management.copy.domain;

import com.products_management.copy.domain.models.CopyEquivalencia;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests para el modelo de dominio CopyEquivalencia.
 */
class CopyEquivalenciaTest {

    @Test
    void builderDebeCrearEquivalenciaConCampos() {
        CopyEquivalencia eq = CopyEquivalencia.builder()
                .tabla("account")
                .idViejo("100")
                .idNuevo("200")
                .build();

        assertThat(eq.getTabla()).isEqualTo("account");
        assertThat(eq.getIdViejo()).isEqualTo("100");
        assertThat(eq.getIdNuevo()).isEqualTo("200");
    }

    @Test
    void equivalenciaConTablaDistintaNoSonIguales() {
        CopyEquivalencia eq1 = CopyEquivalencia.builder().tabla("account").idViejo("1").idNuevo("2").build();
        CopyEquivalencia eq2 = CopyEquivalencia.builder().tabla("tax").idViejo("1").idNuevo("2").build();

        assertThat(eq1).isNotEqualTo(eq2);
    }
}
