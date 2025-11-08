package com.products_management.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * @brief Configuración Swagger/OpenAPI para documentación de API
 *
 * Configura la documentación automática de la API usando OpenAPI 3.0,
 * incluyendo esquemas de seguridad JWT y metadatos básicos del proyecto.
 */
@Configuration
public class SwaggerConfig {

    /**
     * @brief Configura especificación OpenAPI personalizada
     *
     * Define la configuración completa de OpenAPI incluyendo esquemas de seguridad JWT,
     * información del proyecto y requerimientos de autenticación globales.
     *
     * @return Especificación OpenAPI completamente configurada
     */
    @Bean
    public OpenAPI customizeOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .info(new Info().title("Products Management API")
                        .description("API para la gestión de productos")
                        .version("1.0"));
    }
}
