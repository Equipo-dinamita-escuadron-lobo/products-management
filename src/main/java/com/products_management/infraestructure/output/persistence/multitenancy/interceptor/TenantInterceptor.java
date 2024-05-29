package com.products_management.infraestructure.output.persistence.multitenancy.interceptor;

import com.products_management.infraestructure.output.persistence.multitenancy.utils.TenantContext;
import com.products_management.infraestructure.security.IJwtUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.ModelMap;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.WebRequestInterceptor;



@Component
public class TenantInterceptor implements WebRequestInterceptor {

    @Autowired
    private IJwtUtils jwtUtils;

    @Override
    public void preHandle(WebRequest request) throws Exception {
        TenantContext.setTenantId(jwtUtils.getId());
    }

    @Override
    public void postHandle(WebRequest request, ModelMap model) throws Exception {
        TenantContext.clear();
    }

    @Override
    public void afterCompletion(WebRequest request, Exception ex) throws Exception {

    }
}