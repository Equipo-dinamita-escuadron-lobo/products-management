package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.ProductRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRestController {

    private final IProductServicePort productServicePort;
    private final ProductRestMapperImpl productRestMapper;

    @GetMapping("/findAll/{enterpriseId}")
    public List<ProductResponse> findAll(@PathVariable String enterpriseId) {
      return productRestMapper.toProductResponseList(productServicePort.findAll(enterpriseId));
    }
    @GetMapping("/findAllByUnitOfMeasure/{unitOfMeasureId}")
    public List<ProductResponse> findAllByUnitOfMeasure(@PathVariable Long unitOfMeasureId) {
      return productRestMapper.toProductResponseList(productServicePort.findAllByUnitOfMeasure(unitOfMeasureId));
    }
    @GetMapping("/findAllByCategoryId/{categoryId}")
    public List<ProductResponse> findAllByCategory(@PathVariable Long categoryId) {
        return productRestMapper.toProductResponseList(productServicePort.findAllByCategory(categoryId));
    }

    @GetMapping("/findById/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productRestMapper.toProductResponse(productServicePort.findById(id));
    }

    @GetMapping("/findActivate/{enterpriseId}")
    public List<ProductResponse>  findActivate(@PathVariable String enterpriseId) {
        return productRestMapper.toProductResponseList(productServicePort.findActivated(enterpriseId));
    }

    @PostMapping("/create")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productRestMapper.toProductResponse(
                        productServicePort.create(productRestMapper.toProduct(productCreateRequest))));
    }
    @PutMapping("/update/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return productRestMapper.toProductResponse(
                productServicePort.update(id, productRestMapper.toProduct(productCreateRequest)));
    }

    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        productServicePort.changeState(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        productServicePort.deleteById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        productServicePort.deleteAll();
    }

}
