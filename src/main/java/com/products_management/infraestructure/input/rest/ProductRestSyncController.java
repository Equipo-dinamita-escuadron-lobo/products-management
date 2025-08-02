package com.products_management.infraestructure.input.rest;

import java.time.Instant;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.products_management.application.dto.ProductSyncDto;
import com.products_management.application.ports.input.IProductSyncServicePort;


import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/sync")
public class ProductRestSyncController {
    private final IProductSyncServicePort productSyncServicePort;

    @GetMapping("/findByEnterpriseId/{enterpriseId}")
    public List<ProductSyncDto> findByEnterpriseId(
            @PathVariable String enterpriseId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant since)
    {   
        List<ProductSyncDto> productSyncDtos = productSyncServicePort.findByEnterpriseId(enterpriseId, since);
        return productSyncDtos;
    }

}
