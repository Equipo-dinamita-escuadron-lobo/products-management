package com.products_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode;
import org.springframework.scheduling.annotation.EnableAsync;

import com.products_management.infraestructure.config.FileUploadProperties;

/**
 * Clase principal de la aplicación de gestión de productos.
 * Utiliza Spring Boot para inicializar la aplicación.
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableAsync
@EnableConfigurationProperties(FileUploadProperties.class)
@EnableSpringDataWebSupport(pageSerializationMode = PageSerializationMode.VIA_DTO)
public class ProductsManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductsManagementApplication.class, args);
		
		
	}
}
