package com.products.infraestructure.input.Rest;

import com.products.aplication.input.IProductCreateManagerPort;
import com.products.aplication.input.IProductGetAllManagerPort;
import com.products.aplication.input.IProductSearchManagerPort;
import com.products.domain.models.Product;
import com.products.infraestructure.input.Rest.Data.Request.ProductCreateRequest;
import com.products.infraestructure.input.Rest.Data.Response.ProductCreateResponse;
import com.products.infraestructure.input.Rest.Data.Response.ProductGetByIdResponse;
import com.products.infraestructure.input.Rest.Mapper.IProductRestMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@AllArgsConstructor
public class ProductController{
    private final IProductSearchManagerPort productSearchManagerPort;
    private final IProductCreateManagerPort productCreateManagerPort;
    private final IProductGetAllManagerPort productGetAllManagerPort;
    private final IProductRestMapper productRestMapper;

    @GetMapping("/GetAll")
    public ResponseEntity<List<Product>> getAllProduct(){
        return ResponseEntity.ok(productGetAllManagerPort.getAllProducts());
    }
    @GetMapping("/GetById{id}")
    public ResponseEntity<ProductGetByIdResponse> getByIdProduct(@RequestParam String id){
        return ResponseEntity.ok(productRestMapper.toResponse(productSearchManagerPort.getByIdProduct(id)));
    }

    @PostMapping("/CreateProduct")
    public ResponseEntity<ProductCreateResponse> createProduct(@RequestBody ProductCreateRequest productCreateRequest) {
        Product product = productCreateManagerPort.createProduct(productRestMapper.toProduct(productCreateRequest));
        ProductCreateResponse productCreateResponse = productRestMapper.toCreateProductResponse(product);
        return ResponseEntity.ok(productCreateResponse);
    }
}