package com.products_management.infraestructure.output.multitenancy.async;

import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

import com.products_management.infraestructure.output.multitenancy.utils.TenantContext;

/**
 * @brief Decorador de tareas consciente de tenant para operaciones asíncronas
 *
 * Captura el ID del tenant actual y lo establece en el contexto del hilo
 * antes de ejecutar tareas asíncronas, garantizando aislamiento de datos.
 */
public class TenantAwareTaskDecorator implements TaskDecorator {

    /**
     * @brief Decora tarea runnable con sensibilidad al tenant
     * @param runnable tarea runnable a decorar
     * @return tarea decorada con contexto de tenant preservado
     */
    @Override
    @NonNull
    public Runnable decorate(@NonNull Runnable runnable) {
        String tenantId = TenantContext.getTenantId(); // Obtiene el ID del tenant actual
        return () -> {
            try {
                TenantContext.setTenantId(tenantId); // Establece el ID del tenant en el contexto de hilo
                runnable.run(); // Ejecuta la tarea
            } finally {
                TenantContext.setTenantId(null); // Limpia el ID del tenant al finalizar la tarea
            }
        };
    }
}
