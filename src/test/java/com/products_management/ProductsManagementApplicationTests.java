package com.products_management;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

/**
 * Clase de prueba para verificar el contexto de la aplicación.
 * Utiliza SpringBootTest para cargar el contexto de la aplicación.
 * ActiveProfiles indica que se utiliza el perfil de configuración "test".
 */
@SpringBootTest
@ActiveProfiles("test")
class ProductsManagementApplicationTests {

	/**
	 * Verifica que el contexto de la aplicación se cargue correctamente.
	 */
	@Test
	void contextLoads() {
		// No se realiza ninguna acción específica en este método de prueba.
		// Su propósito es asegurar que el contexto de la aplicación se cargue sin errores.
	}

}
