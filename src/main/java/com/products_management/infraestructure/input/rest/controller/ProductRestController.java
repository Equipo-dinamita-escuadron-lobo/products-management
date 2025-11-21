package com.products_management.infraestructure.input.rest.controller;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.input.IProductExportUseCase;
import com.products_management.application.ports.input.IProductImportUseCase;
import com.products_management.domain.model.ImportJobStatus;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.dto.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.dto.request.ProductImportRequest;
import com.products_management.infraestructure.input.rest.dto.response.ProductResponse;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductRestMapper;
import com.products_management.infraestructure.utils.ExcelFileNameGenerator;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * @brief Controlador REST principal para gestión de productos
 *
 * Expone endpoints completos CRUD para productos, incluyendo operaciones
 * de importación/exportación masiva (síncronas y asíncronas) y sincronización
 * con sistemas externos.
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRestController {

        private final IProductServicePort productServicePort;
        private final IProductRestMapper productRestMapper;
        private final IProductExportUseCase productExportUseCase;
        private final IProductImportUseCase productImportUseCase;
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
    public ResponseEntity<Map<String, String>> exportProductsAsync(
                    @RequestParam String entId,
                    @RequestParam(required = false) String companyName,
                    @RequestParam(required = false) Boolean status) {

        log.info("Iniciando exportación asíncrona de productos. Empresa: {}, Estado: {}", entId, status);

        com.products_management.infraestructure.input.rest.dto.request.ProductExportRequest request = 
                com.products_management.infraestructure.input.rest.dto.request.ProductExportRequest.builder()
                        .entId(entId)
                        .companyName(companyName)
                        .status(status)
                        .build();

        String jobId = productExportUseCase.exportProductsAsync(request);

        log.info("Exportación asíncrona de productos iniciada. JobId: {}", jobId);

        Map<String, String> response = new HashMap<>();
        response.put("jobId", jobId);
        response.put("message", "Exportación iniciada correctamente");
        response.put("status", "PENDING");

        return ResponseEntity.accepted().body(response);
    }

    @GetMapping("/export/status/{jobId}")
    public ResponseEntity<?> getExportStatus(@PathVariable String jobId) {
        log.info("Consultando estado de exportación de productos. JobId: {}", jobId);

        java.util.Optional<com.products_management.domain.model.ExportJobStatus> jobStatus = 
                productExportUseCase.getExportStatus(jobId);

        if (jobStatus.isEmpty()) {
            log.warn("JobId de exportación no encontrado: {}", jobId);
            return ResponseEntity.notFound().build();
        }

        log.info("Estado de exportación de productos obtenido. JobId: {}, Estado: {}",
                        jobId, jobStatus.get().getStatus());

        return ResponseEntity.ok(jobStatus.get());
    }

    @GetMapping("/export/download/{jobId}")
    public ResponseEntity<Resource> downloadExportedFile(@PathVariable String jobId) {
        log.info("Descargando archivo exportado. JobId: {}", jobId);

        java.util.Optional<com.products_management.domain.model.ExportJobStatus> jobStatus = 
                productExportUseCase.getExportStatus(jobId);

        if (jobStatus.isEmpty()) {
            log.warn("JobId de exportación no encontrado: {}", jobId);
            return ResponseEntity.notFound().build();
        }

        com.products_management.domain.model.ExportJobStatus status = jobStatus.get();

        if (status.getStatus() != com.products_management.domain.enums.ImportStatus.COMPLETED) {
            log.warn("Exportación no completada. JobId: {}, Estado: {}", jobId, status.getStatus());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (status.getFileData() == null) {
            log.error("Archivo no disponible. JobId: {}", jobId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        Resource resource = new org.springframework.core.io.ByteArrayResource(status.getFileData());

        return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + status.getFileName() + "\"")
                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .body(resource);
    }

        @PostMapping("/import/excel")
        public ResponseEntity<Map<String, String>> importProductsFromExcel(
                        @RequestParam String entId,
                        @RequestParam("excelFile") MultipartFile excelFile) {

                log.info("Iniciando importación asíncrona de productos. Empresa: {}, Archivo: {}",
                                entId, excelFile.getOriginalFilename());

                ProductImportRequest request = ProductImportRequest.from(entId, excelFile);
                String jobId = productImportUseCase.importProductsAsync(request);

                log.info("Importación asíncrona iniciada. JobId: {}", jobId);

                Map<String, String> response = new HashMap<>();
                response.put("jobId", jobId);
                response.put("message", "Importación iniciada correctamente");
                response.put("status", "PENDING");

                return ResponseEntity.accepted().body(response);
        }

        @GetMapping("/import/status/{jobId}")
        public ResponseEntity<?> getImportStatus(@PathVariable String jobId) {
                log.info("Consultando estado de importación. JobId: {}", jobId);

                Optional<ImportJobStatus> jobStatus = productImportUseCase.getImportStatus(jobId);

                if (jobStatus.isEmpty()) {
                        log.warn("JobId no encontrado: {}", jobId);
                        return ResponseEntity.notFound().build();
                }

                log.info("Estado de importación obtenido. JobId: {}, Estado: {}",
                                jobId, jobStatus.get().getStatus());

                return ResponseEntity.ok(jobStatus.get());
        }

}
