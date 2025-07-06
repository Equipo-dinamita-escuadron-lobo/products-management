package com.products_management.infraestructure.output.multitenancy.interceptor;

import com.products_management.infraestructure.output.multitenancy.utils.TenantContext;
import com.products_management.infraestructure.security.IJwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;

/**
 * Interceptor para manejar el contexto de inquilino (tenant) basado en JWT.
 * Este interceptor establece el ID del inquilino en el contexto para cada solicitud.
 */
@Component
public class TenantInterceptor implements WebRequestInterceptor {

    @Autowired
    private IJwtUtils jwtUtils;

    /**
     * Pre-manipulación de la solicitud para establecer el ID del inquilino en el contexto.
     * @param request Objeto WebRequest que representa la solicitud actual.
     * @throws Exception Si ocurre algún error durante la manipulación de la solicitud.
     */
    @Override
    public void preHandle(WebRequest request) throws Exception {
        TenantContext.setTenantId(jwtUtils.getId());
    }

    /**
     * Manipulación posterior a la solicitud para limpiar el contexto del inquilino.
     * @param request Objeto WebRequest que representa la solicitud actual.
     * @param model Modelo de datos asociado con la solicitud.
     * @throws Exception Si ocurre algún error durante la manipulación posterior a la solicitud.
     */
    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        TenantContext.clear();
    }

    /**
     * Método llamado después de completar la solicitud, no realiza ninguna acción en este caso.
     * @param request Objeto WebRequest que representa la solicitud actual.
     * @param ex Excepción que puede haber ocurrido durante la solicitud, o nulo si no hay excepción.
     * @throws Exception Si ocurre algún error después de completar la solicitud.
     */
    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {
        // No se realiza ninguna acción en este método para este interceptor.
    }
}
