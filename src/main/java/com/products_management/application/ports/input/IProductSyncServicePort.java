package com.products_management.application.ports.input;

import java.util.List;

import com.products_management.application.dto.ProductSyncDto;

import java.time.Instant;


public interface IProductSyncServicePort {
    List<ProductSyncDto> findByEnterpriseId(String enterpriseId, Instant since);
}
