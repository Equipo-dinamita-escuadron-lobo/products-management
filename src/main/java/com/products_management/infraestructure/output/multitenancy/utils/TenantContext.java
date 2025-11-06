package com.products_management.infraestructure.output.multitenancy.utils;

import lombok.extern.slf4j.Slf4j;

/**
 * @brief Utilidad para gestión de contexto de tenant por hilo
 *
 * Maneja el contexto del tenant usando InheritableThreadLocal,
 * permitiendo aislamiento de datos por empresa en operaciones multihilo.
 */
@Slf4j
public class TenantContext {
    private TenantContext() {}

    private static final InheritableThreadLocal<String> currentTenant = new InheritableThreadLocal<>();

    /**
     * @brief Establece ID del tenant en contexto actual del hilo
     * @param tenantId ID del tenant que se va a establecer
     */
    public static void setTenantId(String tenantId) {
        log.debug("Setting tenantId to " + tenantId);
        currentTenant.set(tenantId);
    }

    /**
     * @brief Obtiene ID del tenant del contexto actual del hilo
     * @return ID del tenant actualmente establecido
     */
    public static String getTenantId() {
        return currentTenant.get();
    }

    /**
     * @brief Limpia contexto del tenant actual, removiendo el ID del tenant
     */
    public static void clear(){
        currentTenant.remove();
    }
}
