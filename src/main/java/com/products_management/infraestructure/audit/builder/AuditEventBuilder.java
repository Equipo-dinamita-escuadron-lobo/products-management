package com.products_management.infraestructure.audit.builder;

import java.time.Instant;
import java.util.Map;
import org.springframework.stereotype.Component;

import com.products_management.infraestructure.audit.annotation.Auditable;
import com.products_management.infraestructure.audit.annotation.OperationType;
import com.products_management.infraestructure.security.IJwtUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuditEventBuilder {

    private final IJwtUtils jwtUtils;

    public OperationEventDto build(
            Auditable auditable,
            OperationType resolvedOperationType,
            String enterpriseId,
            String registerId,
            Map<String, Object> dataObject) {
        return OperationEventDto.builder()
                .enterpriseId(enterpriseId)
                .userId(jwtUtils.getId())
                .userName(jwtUtils.getUsername())
                .userRole(jwtUtils.getRealmRoles())
                .operationType(resolvedOperationType.name())
                .operationAt(Instant.now())
                .moduleName(auditable.moduleName())
                .affectedTable(auditable.affectedTable())
                .registerId(registerId)
                .dataObject(dataObject)
                .build();
    }

}
