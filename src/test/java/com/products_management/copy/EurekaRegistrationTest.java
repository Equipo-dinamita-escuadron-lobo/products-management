package com.products_management.copy;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Verifica que la configuración de Eureka esté presente en el contexto de la aplicación.
 * REQ-PRODUCTS-01 — spring.application.name=PRODUCTS y eureka deshabilitado en perfil test.
 * Fase 2 — Task 2.1 RED.
 */
@SpringBootTest
@ActiveProfiles("test")
class EurekaRegistrationTest {

    @Value("${spring.application.name}")
    private String applicationName;

    @Value("${eureka.client.enabled:true}")
    private boolean eurekaClientEnabled;

    /**
     * Verifica que el nombre de la aplicación sea PRODUCTS (para registro en Eureka).
     */
    @Test
    void applicationNameDebeSerPRODUCTS() {
        assertThat(applicationName).isEqualTo("PRODUCTS");
    }

    /**
     * Verifica que Eureka esté deshabilitado en el perfil test para evitar
     * UnknownHostException al arrancar sin servidor de descubrimiento.
     */
    @Test
    void eurekaDebeEstarDeshabilitadoEnPerfilTest() {
        assertThat(eurekaClientEnabled).isFalse();
    }
}
