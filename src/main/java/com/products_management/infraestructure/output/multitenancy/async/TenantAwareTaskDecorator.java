package com.products_management.infraestructure.output.multitenancy.async;

import org.springframework.core.task.TaskDecorator;
import org.springframework.lang.NonNull;

import com.products_management.infraestructure.output.multitenancy.utils.TenantContext;

/**
 * Decorador de tareas que asegura la sensibilidad al tenant durante la ejecución de tareas asíncronas.
 * Este decorador captura el ID del tenant actual y lo establece en el contexto de hilo antes de ejecutar la tarea.
 */
public class TenantAwareTaskDecorator implements TaskDecorator {

    /**
     * Método para decorar una tarea runnable con sensibilidad al tenant.
     *
     * @param runnable Tarea runnable a decorar.
     * @return Tarea decorada con la sensibilidad al tenant.
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
