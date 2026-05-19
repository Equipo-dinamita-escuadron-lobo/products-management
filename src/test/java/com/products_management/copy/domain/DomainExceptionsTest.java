package com.products_management.copy.domain;

import com.products_management.copy.domain.exceptions.DuplicateCopyJobException;
import com.products_management.copy.domain.exceptions.MissingEquivalenceException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Tests RED para las excepciones de dominio del bounded context copy.
 * Task 3.2 — verifica mensajes y jerarquía de excepciones.
 */
class DomainExceptionsTest {

    @Test
    void missingEquivalenceExceptionDebeContenerTablaYCampo() {
        MissingEquivalenceException ex = new MissingEquivalenceException("account", "100", "inventoryId");

        assertThat(ex.getMessage())
                .contains("account")
                .contains("100")
                .contains("inventoryId");
    }

    @Test
    void missingEquivalenceExceptionDebeExtenderRuntimeException() {
        assertThatThrownBy(() -> { throw new MissingEquivalenceException("tax", "999", "taxId"); })
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(MissingEquivalenceException.class);
    }

    @Test
    void duplicateCopyJobExceptionDebeContenerIdProcesoYFase() {
        DuplicateCopyJobException ex = new DuplicateCopyJobException("proceso-123", 2);

        assertThat(ex.getMessage())
                .contains("proceso-123")
                .contains("2");
    }

    @Test
    void duplicateCopyJobExceptionDebeExtenderRuntimeException() {
        assertThatThrownBy(() -> { throw new DuplicateCopyJobException("x", 1); })
                .isInstanceOf(RuntimeException.class)
                .isInstanceOf(DuplicateCopyJobException.class);
    }
}
