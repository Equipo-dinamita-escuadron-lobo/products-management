package com.products_management.infraestructure.output.persistence;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Component;

import com.products_management.application.ports.output.IProductSyncPort;
import com.products_management.application.dto.ProductSyncDto;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductSyncMapper;
import com.products_management.infraestructure.output.persistence.repository.IProductRepository;


import lombok.RequiredArgsConstructor;

/**
 * @brief Adaptador para operaciones de sincronización de productos
 *
 * Implementa IProductSyncPort para proporcionar datos de productos
 * modificados después de una fecha específica para sincronización.
 */
@Component
@RequiredArgsConstructor
public class ProductSyncAdapter implements IProductSyncPort{

    private final IProductRepository productRepository;
    private final IProductSyncMapper productSyncMapper;
    
    @Override
    public List<ProductSyncDto> findByEnterpriseId(String enterpriseId, Instant since) {
        return productSyncMapper.toDto(productRepository.findByEnterpriseIdAndLastModifiedDateAfter(enterpriseId, since));
    }

}
    
