package com.products_management.application.ports.output;

import java.util.List;

import com.products_management.application.dto.ProductSyncDto;

import java.time.Instant;

public interface IProductSyncPort {
    List<ProductSyncDto> findByEnterpriseId(String enterpriseId, Instant since);
}
