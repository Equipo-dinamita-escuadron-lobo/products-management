package com.products_management.infraestructure.input.rest;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductTypeRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductTypeRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductTypeResponse;
import com.products_management.application.ports.input.IProductTypeServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de tipos de producto.
 */
@CrossOrigin("*") // Allow cross-origin requests
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
        Optional<ProductType> productTypeOptional = productTypeService.findById(id);
        if (productTypeOptional.isPresent()) {
            ProductTypeResponse response = productTypeMapper.toProductTypeResponse(productTypeOptional.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
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
