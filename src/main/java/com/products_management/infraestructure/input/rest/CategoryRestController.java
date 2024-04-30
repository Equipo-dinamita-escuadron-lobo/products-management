package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.ICategoryServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.CategoryRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final ICategoryServicePort categoryServicePort;
    private final CategoryRestMapperImpl categoryRestMapper;

    @GetMapping("/findAll")
    public List<CategoryResponse> findAll() {
        return categoryRestMapper.toCategoryResponseList(categoryServicePort.findAll());
    }

    @GetMapping("/findById/{id}")
    public CategoryResponse findById(@PathVariable Long id) {
        return categoryRestMapper.toCategoryResponse(categoryServicePort.findById(id));
    }

    @GetMapping("/findActivate")
    public List<CategoryResponse>  findActivate() {
        return categoryRestMapper.toCategoryResponseList(categoryServicePort.findActivated());
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryRestMapper.toCategoryResponse(
                        categoryServicePort.create(categoryRestMapper.toCategory(categoryCreateRequest))));
    }
    @PutMapping("/update/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return categoryRestMapper.toCategoryResponse(
                categoryServicePort.update(id, categoryRestMapper.toCategory(categoryCreateRequest)));
    }
    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        categoryServicePort.changeState(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryServicePort.deleteById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        categoryServicePort.deleteAll();
    }
}
