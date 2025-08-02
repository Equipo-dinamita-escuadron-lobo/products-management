package com.products_management.application.service;

import java.time.Instant;
import java.util.List;

import org.springframework.stereotype.Service;

import com.products_management.application.dto.ProductSyncDto;
import com.products_management.application.ports.input.IProductSyncServicePort;
import com.products_management.application.ports.output.IProductSyncPort;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductSyncService implements IProductSyncServicePort{

    private final IProductSyncPort productSyncPort;

    @Override
    public List<ProductSyncDto> findByEnterpriseId(String enterpriseId, Instant since) {
        return productSyncPort.findByEnterpriseId(enterpriseId, since);
    }
    
}
