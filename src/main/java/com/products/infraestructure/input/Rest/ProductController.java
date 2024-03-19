package com.products.infraestructure.input.Rest;

import com.products.aplication.input.IProductManagerPort;
import com.products.domain.models.Product;
import com.products.infraestructure.input.Rest.Data.Request.ProductRequest;
import com.products.infraestructure.input.Rest.Data.Response.ProductResponse;
import com.products.infraestructure.input.Rest.Mapper.IProductRestMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
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

    @PostMapping("/CreateProduct")
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = productManagerPort.createProduct(productRestMapper.toProduct(productRequest));
        ProductResponse productResponse = productRestMapper.toResponse(product);
        return ResponseEntity.ok(productResponse);
    }
}