package com.products_management.infraestructure.output.multitenancy.async;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
/**
 * @brief Configuración de ejecución asíncrona con soporte para multitenancy
 *
 * Habilita procesamiento asíncrono con ThreadPoolTaskExecutor personalizado
 * que incluye decorador consciente de tenant para aislamiento de datos.
 */
@Configuration
@EnableAsync
@Role(BeanDefinition.ROLE_INFRASTRUCTURE)
public class AsyncConfig implements AsyncConfigurer {

    /**
     * @brief Configura ThreadPoolTaskExecutor para tareas asíncronas
     * @return executor configurado con decorador consciente de tenant
     */
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(7); // Tamaño inicial del pool de hilos
        executor.setMaxPoolSize(42); // Tamaño máximo del pool de hilos
        executor.setQueueCapacity(11); // Capacidad máxima de la cola de tareas en espera
        executor.setThreadNamePrefix("TenantAwareTaskExecutor-"); // Prefijo para nombres de hilos
        executor.setTaskDecorator(new TenantAwareTaskDecorator()); // Decorador de tareas para la sensibilidad al tenant
        executor.initialize();

        return executor;
    }

}