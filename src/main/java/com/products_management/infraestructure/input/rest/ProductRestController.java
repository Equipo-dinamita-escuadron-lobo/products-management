package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.input.IProductExportUseCase;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import com.products_management.infraestructure.utils.ExcelFileNameGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

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
        private final IProductExportUseCase productExportUseCase;
        private final ExcelFileNameGenerator fileNameGenerator;

        @GetMapping("/findAll")
        public ResponseEntity<Page<ProductResponse>> findAll(
                        @RequestParam String enterpriseId,
                        @RequestParam(required = false) Optional<Integer> numPage,
                        @RequestParam(required = false) Optional<Integer> size,
                        @RequestParam(defaultValue = "name") String sortField,
                        @RequestParam(defaultValue = "asc") String sortOrder,
                        @RequestParam(required = false) String search) {

                Page<Product> productPage = productServicePort.findAllPaginated(enterpriseId, numPage, size, sortField, sortOrder, Optional.ofNullable(search));
                Page<ProductResponse> responsePage = productPage.map(productRestMapper::toProductResponse);
                return new ResponseEntity<>(responsePage, HttpStatus.OK);
        }

        @GetMapping("/findById/{id}/{enterpriseId}")
        public ProductResponse findById(@PathVariable Long id, @PathVariable String enterpriseId) {
                return productRestMapper.toProductResponse(productServicePort.findById(id, enterpriseId));
        }

        @GetMapping("/findActivate")
        public ResponseEntity<Page<ProductResponse>> findActivate(
                        @RequestParam String enterpriseId,
                        @RequestParam(required = false) Optional<Integer> numPage,
                        @RequestParam(required = false) Optional<Integer> size) {

                Page<Product> productPage = productServicePort.findActivatedPaginated(enterpriseId, numPage, size);
                Page<ProductResponse> responsePage = productPage.map(productRestMapper::toProductResponse);
                return new ResponseEntity<>(responsePage, HttpStatus.OK);
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

        @GetMapping("/template/excel")
        public ResponseEntity<Resource> exportProductTemplate(@RequestParam String entId) {
                Resource templateFile = productExportUseCase.exportProductTemplateWithValidations(entId);
                String filename = fileNameGenerator.generateTemplateFileName();

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                                .body(templateFile);
        }

        @GetMapping("/export/excel")
        public ResponseEntity<Resource> exportProductsWithValidations(
                        @RequestParam String entId,
                        @RequestParam(required = false) String companyName,
                        @RequestParam(required = false) Boolean status) {

                Resource excelFile = productExportUseCase.exportProductsWithValidations(entId, status);
                String filename = fileNameGenerator.generateExportFileName(entId, companyName, status);

                return ResponseEntity.ok()
                                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                                .body(excelFile);
        }

}
