package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.ICategoryServicePort;
import com.products_management.infraestructure.input.rest.mapper.interfaces.ICategoryRestMapper;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de categorías de productos.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryRestController {

        private final ICategoryServicePort categoryServicePort;
        private final ICategoryRestMapper categoryRestMapper;

        @GetMapping("/findAll/{enterpriseId}")
        public List<CategoryResponse> findAll(@PathVariable String enterpriseId) {
                return categoryRestMapper.toCategoryResponseList(categoryServicePort.findAll(enterpriseId));
        }

        @GetMapping("/findById/{enterpriseId}/{id}")
        public CategoryResponse findById(@PathVariable String enterpriseId, @PathVariable Long id) {
                return categoryRestMapper.toCategoryResponse(categoryServicePort.findById(enterpriseId, id));
        }

        @GetMapping("/findActivate/{enterpriseId}")
        public List<CategoryResponse> findActivate(@PathVariable String enterpriseId) {
                return categoryRestMapper.toCategoryResponseList(categoryServicePort.findActivated(enterpriseId));
        }

        @PostMapping("/create")
        public ResponseEntity<CategoryResponse> create(
                        @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(categoryRestMapper.toCategoryResponse(
                                                categoryServicePort.create(
                                                                categoryRestMapper.toCategory(categoryCreateRequest))));
        }

        @PutMapping("/update/{enterpriseId}/{id}")
        public CategoryResponse update(@PathVariable String enterpriseId, @PathVariable Long id,
                        @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
                return categoryRestMapper.toCategoryResponse(
                                categoryServicePort.update(enterpriseId, id, categoryRestMapper.toCategory(categoryCreateRequest)));
        }

        @PutMapping("/changeState/{enterpriseId}/{id}")
        public void changeState(@PathVariable String enterpriseId, @PathVariable Long id) {
                categoryServicePort.changeState(enterpriseId, id);
        }

        @DeleteMapping("/delete/{enterpriseId}/{id}")
        public void deleteById(@PathVariable String enterpriseId, @PathVariable Long id) {
                categoryServicePort.deleteById(enterpriseId, id);
        }
}
