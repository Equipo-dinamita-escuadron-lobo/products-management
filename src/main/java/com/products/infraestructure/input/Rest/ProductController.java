package com.products.infraestructure.input.Rest;

import com.products.aplication.input.IProductManagerPort;
import com.products.domain.models.Product;
import com.products.infraestructure.input.Rest.Data.Request.ProductRequest;
import com.products.infraestructure.input.Rest.Data.Response.ProductResponse;
import com.products.infraestructure.input.Rest.Mapper.IProductRestMapper;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/api/product")
@AllArgsConstructor
public class ProductController{
    private final IProductManagerPort productManagerPort;
    private final IProductRestMapper productRestMapper;

    @GetMapping("/GetAll")
    public ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(productManagerPort.getAllProducts());
    }
    @GetMapping("/GetById{id}")
    public ResponseEntity<ProductResponse> getByIdProduct(@RequestParam String id){
        return ResponseEntity.ok(productRestMapper.toResponse(productManagerPort.getByIdProduct(id)));
    }

    /*@PostMapping("/CreateProduct")
    public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        Product product = productManagerPort.createProduct(productRestMapper.toProduct(productRequest));
        ProductResponse productResponse = productRestMapper.toResponse(product);
        return ResponseEntity.ok(productResponse);
    }*/

    @PostMapping("/CreateProduct")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductRequest productRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errors);
        } else {
            Product product = productManagerPort.createProduct(productRestMapper.toProduct(productRequest));
            ProductResponse productResponse = productRestMapper.toResponse(product);
            return ResponseEntity.ok(productResponse);
        }
    }
}