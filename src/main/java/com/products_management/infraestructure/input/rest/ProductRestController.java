package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import com.products_management.infraestructure.utils.PaginationHelper;

import java.util.List;
import java.util.Optional;

/**
 * Controlador REST para la gestión de productos.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRestController {

        private final IProductServicePort productServicePort;
        private final IProductRestMapper productRestMapper;

        @GetMapping("/findAll")
        public ResponseEntity<Page<ProductResponse>> findAll(
                        @RequestParam String enterpriseId,
                        @RequestParam(required = false) Optional<Integer> numPage,
                        @RequestParam(required = false) Optional<Integer> size,
                        @RequestParam(defaultValue = "name") String sortField,
                        @RequestParam(defaultValue = "asc") String sortOrder,
                        @RequestParam(required = false) String search) {

                // Contar total de registros (con o sin filtro)
                long totalRecords = (search != null && !search.trim().isEmpty())
                                ? productServicePort.countByEnterpriseIdWithFilters(enterpriseId, search)
                                : productServicePort.countByEnterpriseId(enterpriseId);

                // Crear Pageable flexible
                Pageable pageable = PaginationHelper.createFlexiblePageable(numPage, size, totalRecords);

                // Obtener página de datos (con o sin filtro)
                Page<Product> productPage = (search != null && !search.trim().isEmpty())
                                ? productServicePort.findAllWithFilters(enterpriseId, search, pageable.getPageNumber(),
                                                pageable.getPageSize(), sortField, sortOrder)
                                : productServicePort.findAllWithFilters(enterpriseId, null, pageable.getPageNumber(),
                                                pageable.getPageSize(), sortField, sortOrder);

                // Mapear a ProductResponse
                Page<ProductResponse> responsePage = productPage.map(productRestMapper::toProductResponse);

                return new ResponseEntity<>(responsePage, HttpStatus.OK);
        }

        @GetMapping("/findById/{id}/{enterpriseId}")
        public ProductResponse findById(@PathVariable Long id, @PathVariable String enterpriseId) {
                return productRestMapper.toProductResponse(productServicePort.findById(id, enterpriseId));
        }

        @GetMapping("/findActivate/{enterpriseId}")
        public List<ProductResponse> findActivate(@PathVariable String enterpriseId) {
                return productRestMapper.toProductResponseList(productServicePort.findActivated(enterpriseId));
        }

        @PostMapping("/create")
        public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(productRestMapper.toProductResponse(
                                                productServicePort.create(
                                                                productRestMapper.toProduct(productCreateRequest))));
        }

        @PutMapping("/update/{id}")
        public ProductResponse update(@PathVariable Long id,
                        @Valid @RequestBody ProductCreateRequest productCreateRequest) {
                return productRestMapper.toProductResponse(
                                productServicePort.update(id, productRestMapper.toProduct(productCreateRequest),
                                                productCreateRequest.getEnterpriseId()));
        }

        @PutMapping("/changeState/{id}/{enterpriseId}")
        public void changeState(@PathVariable Long id, @PathVariable String enterpriseId) {
                productServicePort.changeState(id, enterpriseId);
        }

        @DeleteMapping("/delete/{id}/{enterpriseId}")
        public void deleteById(@PathVariable Long id, @PathVariable String enterpriseId) {
                productServicePort.deleteById(id, enterpriseId);
        }

}
