package com.products_management.infraestructure.output.multitenancy;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import com.products_management.infraestructure.output.multitenancy.utils.TenantContext;

import java.util.Map;

/**
 * @brief Resolver de identificador de tenant actual para Hibernate
 *
 * Determina dinámicamente el tenant actual basado en TenantContext,
 * permitiendo aislamiento de datos por empresa en operaciones de base de datos.
 */
@SuppressWarnings("rawtypes")
@Component
class CurrentTenantIdentifierResolverImpl implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    /**
     * @brief Resuelve identificador del tenant actual
     * @return ID del tenant actual o "BOOTSTRAP" si no hay tenant configurado
     */
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId = TenantContext.getTenantId();
        if (!ObjectUtils.isEmpty(tenantId)) {
            return tenantId;
        } else {
            // Permite inicializar EntityManagerFactory sin un inquilino específico
            return "BOOTSTRAP";
        }
    }

    /**
     * @brief Indica si validar sesiones actuales existentes
     * @return siempre true para validar sesiones existentes
     */
    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }

    /**
     * @brief Personaliza propiedades de Hibernate con este resolver
     * @param hibernateProperties mapa de propiedades de Hibernate a personalizar
     */
    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

}
