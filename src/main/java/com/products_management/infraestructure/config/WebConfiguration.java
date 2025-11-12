package com.products_management.infraestructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.products_management.infraestructure.output.multitenancy.interceptor.TenantInterceptor;

import lombok.RequiredArgsConstructor;

/**
 * @brief Configuración web MVC para multitenancy
 *
 * Configura interceptores para manejar lógica de multitenancy en requests web,
 * permitiendo identificar y aislar datos por empresa/tenant automáticamente.
 */
@RequiredArgsConstructor
@Configuration
public class WebConfiguration implements WebMvcConfigurer {

    private final TenantInterceptor tenantInterceptor;

    @Override
    public void addInterceptors(@SuppressWarnings("null") InterceptorRegistry registry) {
        registry.addWebRequestInterceptor(tenantInterceptor);
    }

}
