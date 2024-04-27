package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.UnitOfMeasureRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unit-measures")
public class UnitMeasureRestController {
    private final IUnitOfMeasureServicePort unitOfMeasureServicePort;
    private final UnitOfMeasureRestMapperImpl unitOfMeasureRestMapper;

    @GetMapping("/findAll")
    public List<UnitOfMeasureResponse> findAll() {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponseList(unitOfMeasureServicePort.findAll());
    }

    @GetMapping("/findById/{id}")
    public UnitOfMeasureResponse findById(@PathVariable Long id) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponse(unitOfMeasureServicePort.findById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<UnitOfMeasureResponse> create(@Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                        unitOfMeasureServicePort.create(unitOfMeasureRestMapper.toUnitOfMeasure(unitOfMeasureCreateRequest))));
    }
    
    @PutMapping("/update/{id}")
    public UnitOfMeasureResponse update(@PathVariable Long id, @Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                unitOfMeasureServicePort.update(id, unitOfMeasureRestMapper.toUnitOfMeasure(unitOfMeasureCreateRequest)));
    }

    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        unitOfMeasureServicePort.changeState(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        unitOfMeasureServicePort.deleteById(id);
    }

    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        unitOfMeasureServicePort.deleteAll();
    }
}
