package com.products_management.application.service.product;

import org.springframework.stereotype.Service;

import com.products_management.application.ports.input.IProductUsagePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.exception.product.ProductNotFoundException;
import com.products_management.domain.model.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @brief Servicio para gestión de uso de productos
 *
 * Maneja la lógica de negocio relacionada con el contador de uso de productos
 * cuando son utilizados por otros servicios.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ProductUsageService implements IProductUsagePort {

    private final IProductPersistencePort productPersistencePort;

    @Override
    public void incrementUsageCount(Long productId, String enterpriseId) {
        log.info("Incrementing usage count for productId: {} in enterprise: {}", productId, enterpriseId);
        
        Product product = productPersistencePort.findByIdAndEnterpriseId(productId, enterpriseId)
                .orElseThrow(() -> {
                    log.warn("Product not found: {} in enterprise: {}", productId, enterpriseId);
                    return new ProductNotFoundException();
                });
        
        product.incrementUsageCount();
        productPersistencePort.create(product);
        
        log.info("Usage count incremented successfully for productId: {}. New count: {}", 
                 productId, product.getUsageCount());
    }
}

