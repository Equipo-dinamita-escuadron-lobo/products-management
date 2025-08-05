package com.products_management.infraestructure.input.rest;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductTypeRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductTypeRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductTypeResponse;

import com.products_management.application.ports.input.IProductTypeServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de tipos de producto.
 */
@RestController
@RequestMapping("/api/product-types")
@RequiredArgsConstructor // Use constructor injection
public class ProductTypeRestController {

    private final IProductTypeServicePort productTypeService;
    private final IProductTypeRestMapper productTypeMapper;

    @PostMapping
    public ResponseEntity<ProductTypeResponse> createProductType(@RequestBody ProductTypeRequest productTypeRequest) {
        ProductType productType = productTypeMapper.toProductType(productTypeRequest);
        ProductType createdProductType = productTypeService.createProductType(productType);
        ProductTypeResponse response = productTypeMapper.toProductTypeResponse(createdProductType);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.listAllProductTypes();
        List<ProductTypeResponse> responses = productTypes.stream()
                .map(productTypeMapper::toProductTypeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/enterprise/{enterpriseId}")
    public ResponseEntity<List<ProductTypeResponse>> getProductTypesByEnterpriseId(@PathVariable String enterpriseId) {
        List<ProductType> productTypes = productTypeService.getProductTypesByEnterpriseId(enterpriseId);
        List<ProductTypeResponse> responses = productTypes.stream()
                .map(productTypeMapper::toProductTypeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> getProductTypeById(@PathVariable Long id) {
        ProductType productType = productTypeService.getProductTypeById(id);
        ProductTypeResponse response = productTypeMapper.toProductTypeResponse(productType);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> updateProductType(
            @PathVariable Long id,
            @RequestBody ProductTypeRequest productTypeRequest) {
        ProductType productType = productTypeMapper.toProductType(productTypeRequest);
        ProductType updatedProductType = productTypeService.updateProductType(id, productType);
        ProductTypeResponse response = productTypeMapper.toProductTypeResponse(updatedProductType);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.noContent().build();
    }
}
