package com.products_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Clase principal de la aplicación de gestión de productos.
 * Utiliza Spring Boot para inicializar la aplicación.
 */
@SpringBootApplication
@EnableDiscoveryClient
public class ProductsManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsManagementApplication.class, args);
		
		
	}
}
