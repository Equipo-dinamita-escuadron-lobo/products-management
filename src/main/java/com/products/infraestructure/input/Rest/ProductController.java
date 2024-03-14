package com.products.infraestructure.input.Rest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.products.aplication.input.IProductSearchManagerPort;
import com.products.aplication.input.IProductCreateManagerPort;
import com.products.domain.models.Product;
import com.products.infraestructure.input.Rest.Data.Response.ProductGetByIdResponse;
import com.products.infraestructure.input.Rest.Mapper.IProductRestMapper;
import com.products.infraestructure.input.Rest.Data.Request.ProductCreateRequest;
import com.products.infraestructure.input.Rest.Data.Response.ProductCreateResponse;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController{
    private final IProductSearchManagerPort productSearchManagerPort;
    private final IProductCreateManagerPort productCreateManagerPort;
    private final IProductRestMapper productRestMapper;

    @GetMapping("/{id}")
    public ResponseEntity<ProductGetByIdResponse> getByIdProduct(@RequestParam String id){
        return ResponseEntity.ok(productRestMapper.toResponse(productSearchManagerPort.getByIdProduct(id)));
    }
    
    @PostMapping("/")
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        Product product = productCreateManagerPort.createProduct(productRestMapper.toProduct(productCreateRequest));
        ProductCreateResponse productCreateResponse = productRestMapper.toCreateProductResponse(product);
        return ResponseEntity.ok(productCreateResponse);
    }
}
