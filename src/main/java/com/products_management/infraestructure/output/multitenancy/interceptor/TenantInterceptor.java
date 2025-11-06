package com.products_management.infraestructure.output.multitenancy.interceptor;

import com.products_management.infraestructure.output.multitenancy.utils.TenantContext;
import com.products_management.infraestructure.security.IJwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * @brief Interceptor para gestión de contexto de tenant basado en JWT
 *
 * Establece y limpia el ID del tenant en el contexto para cada solicitud HTTP,
 * garantizando aislamiento de datos por empresa durante toda la operación.
 */
@Component
public class TenantInterceptor implements WebRequestInterceptor {

    @Autowired
    private IJwtUtils jwtUtils;

    /**
     * @brief Establece ID del tenant en contexto antes de procesar solicitud
     * @param request objeto WebRequest que representa la solicitud actual
     */
    @Override
    public void preHandle(WebRequest request) throws Exception {
        TenantContext.setTenantId(jwtUtils.getId());
    }

    /**
     * @brief Limpia contexto del tenant después de procesar solicitud
     * @param request objeto WebRequest que representa la solicitud actual
     * @param model modelo de datos asociado con la solicitud
     */
    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        TenantContext.clear();
    }

    /**
     * @brief Método llamado después de completar solicitud (sin acción)
     * @param request objeto WebRequest que representa la solicitud actual
     * @param ex excepción que puede haber ocurrido durante la solicitud
     */
    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        // No se realiza ninguna acción en este método para este interceptor.
    }
}
