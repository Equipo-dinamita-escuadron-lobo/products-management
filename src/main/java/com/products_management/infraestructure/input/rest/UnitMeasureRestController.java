package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.domain.model.UnitOfMeasure;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IUnitOfMeasureRestMapper;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;
import com.products_management.infraestructure.utils.PaginationHelper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controlador REST para la gestión de unidades de medida.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unit-measures")
public class UnitMeasureRestController {

        private final IUnitOfMeasureServicePort unitOfMeasureServicePort;
        private final IUnitOfMeasureRestMapper unitOfMeasureRestMapper;

       
        @GetMapping("/findAll")
        public ResponseEntity<Page<UnitOfMeasureResponse>> findAll(
                        @RequestParam String enterpriseId,
                        @RequestParam(required = false) String search,
                        @RequestParam(required = false) Optional<Integer> numPage,
                        @RequestParam(required = false) Optional<Integer> size,
                        @RequestParam(defaultValue = "name") String sortField,
                        @RequestParam(defaultValue = "asc") String sortOrder) {

                long totalRecords = (search != null && !search.trim().isEmpty())
                                ? unitOfMeasureServicePort.countByEntIdAndSearch(enterpriseId, search)
                                : unitOfMeasureServicePort.countAllUnitOfMeasuresByEntId(enterpriseId);

                Pageable pageable = PaginationHelper.createFlexiblePageable(numPage, size, totalRecords);

                Page<UnitOfMeasure> page = (search != null && !search.trim().isEmpty())
                                ? unitOfMeasureServicePort.findByEntIdAndSearch(enterpriseId, search,
                                                pageable.getPageNumber(), pageable.getPageSize(), sortField, sortOrder)
                                : unitOfMeasureServicePort.getAllUnitOfMeasuresByWithSort(enterpriseId,
                                                pageable.getPageNumber(), pageable.getPageSize(), sortField, sortOrder);

                Page<UnitOfMeasureResponse> response = page.map(unitOfMeasureRestMapper::toUnitOfMeasureResponse);

                return ResponseEntity.ok(response);
        }

        @GetMapping("/findActivate")
        public ResponseEntity<Page<UnitOfMeasureResponse>> findActivate(
                        @RequestParam String enterpriseId,
                        @RequestParam(required = false) Optional<Integer> numPage,
                        @RequestParam(required = false) Optional<Integer> size) {

                long totalRecords = unitOfMeasureServicePort.countActiveUnitOfMeasuresByEntId(enterpriseId);

                Pageable pageable = PaginationHelper.createFlexiblePageable(numPage, size, totalRecords);

                Page<UnitOfMeasure> page = unitOfMeasureServicePort.getAllActiveUnitOfMeasuresBy(enterpriseId,
                                pageable.getPageNumber(), pageable.getPageSize());

                Page<UnitOfMeasureResponse> response = page.map(unitOfMeasureRestMapper::toUnitOfMeasureResponse);

                return ResponseEntity.ok(response);
        }

        @GetMapping("/findById/{id}")
        public UnitOfMeasureResponse findById(@PathVariable Long id, @RequestParam String enterpriseId) {
                return unitOfMeasureRestMapper.toUnitOfMeasureResponse(unitOfMeasureServicePort.findByIdAndEnterpriseId(id, enterpriseId));
        }

        @PostMapping("/create")
        public ResponseEntity<UnitOfMeasureResponse> create(
                        @Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
                return ResponseEntity.status(HttpStatus.CREATED)
                                .body(unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                                                unitOfMeasureServicePort
                                                                .create(unitOfMeasureRestMapper.toUnitOfMeasure(
                                                                                unitOfMeasureCreateRequest))));
        }

        @PutMapping("/update/{id}")
        public UnitOfMeasureResponse update(@PathVariable Long id,
                        @RequestParam String enterpriseId,
                        @Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
                UnitOfMeasure unitOfMeasure = unitOfMeasureRestMapper.toUnitOfMeasure(unitOfMeasureCreateRequest);
                return unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                                unitOfMeasureServicePort.update(id, enterpriseId, unitOfMeasure));
        }

        @PutMapping("/changeState/{id}")
        public void changeState(@PathVariable Long id, @RequestParam String enterpriseId) {
                unitOfMeasureServicePort.changeState(id, enterpriseId);
        }

        @DeleteMapping("/delete/{id}")
        public void deleteById(@PathVariable Long id, @RequestParam String enterpriseId) {
                unitOfMeasureServicePort.deleteById(id, enterpriseId);
        }

}
