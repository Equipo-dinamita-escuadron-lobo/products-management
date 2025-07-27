package com.products_management.infraestructure.input.rest;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductRestSyncMapper;
import com.products_management.infraestructure.input.rest.model.response.ProductSyncDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products/sync")
public class ProductRestSyncController {
    private final IProductServicePort productServicePort;
    private final IProductRestSyncMapper productRestSyncMapper;

    @GetMapping("/findAll/{enterpriseId}")
    public List<ProductSyncDto> findAll(@PathVariable String enterpriseId) {
        List<ProductSyncDto> productSyncDtos = productRestSyncMapper.toProductSyncDto(
                productServicePort.findAll(enterpriseId));
        return productSyncDtos;
    }

}
