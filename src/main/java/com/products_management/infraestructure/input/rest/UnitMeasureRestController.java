package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.UnitOfMeasureRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Operation(summary = "Obtener todas las unidades de medida de una empresa", description = "Este endpoint devuelve una lista de todas las unidades de medida asociadas a una empresa específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Unidades de medida obtenidas con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findAll/{enterpriseId}")
    public List<UnitOfMeasureResponse> findAll(@PathVariable String enterpriseId) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponseList(unitOfMeasureServicePort.findAll(enterpriseId));
    }

    @Operation(summary = "Obtener todas las unidades de medida activadas de una empresa", description = "Este endpoint devuelve una lista de todas las unidades de medida activadas asociadas a una empresa específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Unidades de medida activadas obtenidas con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findActivate/{enterpriseId}")
    public List<UnitOfMeasureResponse> findActivate(@PathVariable String enterpriseId) {
        return unitOfMeasureRestMapper
                .toUnitOfMeasureResponseList(unitOfMeasureServicePort.findActivated(enterpriseId));
    }

    @Operation(summary = "Obtener una unidad de medida por su identificador", description = "Este endpoint permite obtener una unidad de medida específica mediante su identificador único.", responses = {
            @ApiResponse(responseCode = "200", description = "Unidad de medida encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Unidad de medida no encontrada", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findById/{id}")
    public UnitOfMeasureResponse findById(@PathVariable Long id) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponse(unitOfMeasureServicePort.findById(id));
    }

    @Operation(summary = "Crear una nueva unidad de medida", description = "Este endpoint permite crear una nueva unidad de medida utilizando los datos proporcionados en el cuerpo de la solicitud.", responses = {
            @ApiResponse(responseCode = "201", description = "Unidad de medida creada con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    public ResponseEntity<UnitOfMeasureResponse> create(
            @Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                        unitOfMeasureServicePort
                                .create(unitOfMeasureRestMapper.toUnitOfMeasure(unitOfMeasureCreateRequest))));
    }

    @Operation(summary = "Actualizar una unidad de medida existente", description = "Este endpoint permite actualizar los datos de una unidad de medida existente utilizando el identificador y la información actualizada proporcionada en el cuerpo de la solicitud.", responses = {
            @ApiResponse(responseCode = "200", description = "Unidad de medida actualizada con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Unidad de medida no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/update/{id}")
    public UnitOfMeasureResponse update(@PathVariable Long id,
            @Valid @RequestBody UnitOfMeasureCreateRequest unitOfMeasureCreateRequest) {
        return unitOfMeasureRestMapper.toUnitOfMeasureResponse(
                unitOfMeasureServicePort.update(id,
                        unitOfMeasureRestMapper.toUnitOfMeasure(unitOfMeasureCreateRequest)));
    }

    @Operation(summary = "Cambiar el estado de activación de una unidad de medida", description = "Este endpoint permite cambiar el estado de activación de una unidad de medida utilizando su identificador.", responses = {
            @ApiResponse(responseCode = "200", description = "Estado de la unidad de medida cambiado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Unidad de medida no encontrada", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        unitOfMeasureServicePort.changeState(id);
    }

    @Operation(summary = "Eliminar una unidad de medida por su identificador", description = "Este endpoint permite eliminar una unidad de medida mediante su identificador.", responses = {
            @ApiResponse(responseCode = "200", description = "Unidad de medida eliminada con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Unidad de medida no encontrada", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        unitOfMeasureServicePort.deleteById(id);
    }

    @Operation(summary = "Eliminar todas las unidades de medida", description = "Este endpoint permite eliminar todas las unidades de medida del sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "Unidades de medida eliminadas con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar las unidades de medida", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        unitOfMeasureServicePort.deleteAll();
    }

}
