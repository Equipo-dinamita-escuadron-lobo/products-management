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

/**
 * Controlador REST para la gestión de unidades de medida.
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/unit-measures")
public class UnitMeasureRestController {

    private final IUnitOfMeasureServicePort unitOfMeasureServicePort;
    private final UnitOfMeasureRestMapperImpl unitOfMeasureRestMapper;

    /**
     * Método para obtener todas las unidades de medida de una empresa.
     * @param enterpriseId Identificador de la empresa.
     * @return Lista de respuestas de unidades de medida.
     */
    @GetMapping("/findAll/{enterpriseId}")
    public List<UnitOfMeasureResponse> findAll(@PathVariable String enterpriseId) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponseList(unitOfMeasureServicePort.findAll(enterpriseId));
    }

    /**
     * Método para obtener todas las unidades de medida activadas de una empresa.
     * @param enterpriseId Identificador de la empresa.
     * @return Lista de respuestas de unidades de medida activadas.
     */
    @GetMapping("/findActivate/{enterpriseId}")
    public List<UnitOfMeasureResponse> findActivate(@PathVariable String enterpriseId) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponseList(unitOfMeasureServicePort.findActivated(enterpriseId));
    }

    /**
     * Método para encontrar una unidad de medida por su identificador.
     * @param id Identificador de la unidad de medida.
     * @return Respuesta de la unidad de medida encontrada.
     */
    @GetMapping("/findById/{id}")
    public UnitOfMeasureResponse findById(@PathVariable Long id) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponse(unitOfMeasureServicePort.findById(id));
    }

    /**
     * Método para crear una nueva unidad de medida.
     * @param unitOfMeasureCreateRequest Datos de la unidad de medida a crear.
     * @return Respuesta de la unidad de medida creada con estado HTTP 201.
     */
    @PostMapping("/create")
    public ResponseEntity<UnitOfMeasureResponse> create(@Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                        unitOfMeasureServicePort.create(unitOfMeasureRestMapper.toUnitOfMeasure(unitOfMeasureCreateRequest))));
    }

    /**
     * Método para actualizar una unidad de medida existente.
     * @param id Identificador de la unidad de medida a actualizar.
     * @param unitOfMeasureCreateRequest Datos actualizados de la unidad de medida.
     * @return Respuesta de la unidad de medida actualizada.
     */
    @PutMapping("/update/{id}")
    public UnitOfMeasureResponse update(@PathVariable Long id, @Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                unitOfMeasureServicePort.update(id, unitOfMeasureRestMapper.toUnitOfMeasure(unitOfMeasureCreateRequest)));
    }

    /**
     * Método para cambiar el estado de activación de una unidad de medida.
     * @param id Identificador de la unidad de medida a cambiar de estado.
     */
    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        unitOfMeasureServicePort.changeState(id);
    }

    /**
     * Método para eliminar una unidad de medida por su identificador.
     * @param id Identificador de la unidad de medida a eliminar.
     */
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        unitOfMeasureServicePort.deleteById(id);
    }

    /**
     * Método para eliminar todas las unidades de medida.
     */
    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        unitOfMeasureServicePort.deleteAll();
    }

}
