package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.infraestructure.input.rest.mapper.IProductRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRestController {

    private final IProductServicePort productServicePort;
    private final IProductRestMapper productRestMapper;

    @GetMapping("/findAllProducts")
    public List<ProductResponse> findAllProducts() {
        return productRestMapper.toProductResponseList(productServicePort.findAllProducts());
    }

    @GetMapping("/findByCodeProduct/{code}")
    public ProductResponse findByCodeProduct(@PathVariable String code) {
        return productRestMapper.toProductResponse(productServicePort.findByCodeProduct(code));
    }

    @PostMapping("/createProduct")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productRestMapper.toProductResponse(
                        productServicePort.createProduct(productRestMapper.toProduct(productCreateRequest))));
    }
    @PutMapping("/updateProduct/{code}")
    public ProductResponse updateProduct(@PathVariable String code, @Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return productRestMapper.toProductResponse(
                productServicePort.updateProduct(code, productRestMapper.toProduct(productCreateRequest)));
    }
    @DeleteMapping("/deleteProduct/{code}")
    public void deleteProduct(@PathVariable String code) {
        productServicePort.deleteByCodeProduct(code);
    }
}
