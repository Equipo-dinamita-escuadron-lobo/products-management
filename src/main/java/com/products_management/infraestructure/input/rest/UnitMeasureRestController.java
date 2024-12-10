package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IUnitOfMeasureServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.UnitOfMeasureRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.UnitOfMeasureCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.UnitOfMeasureResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.parameters.RequestBody;

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
        private static boolean unitsInserted = false;

        @Operation(summary = "Obtener todas las unidades de medida de una empresa", description = "Este endpoint devuelve una lista de todas las unidades de medida asociadas a una empresa específica.", responses = {
                        @ApiResponse(responseCode = "200", description = "Unidades de medida obtenidas con éxito", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "404", description = "Empresa no encontrada", content = @Content(mediaType = "application/json"))
        })
        @GetMapping("/findAll/{enterpriseId}")
        public List<UnitOfMeasureResponse> findAll(@PathVariable String enterpriseId) {
                return unitOfMeasureRestMapper
                                .toUnitOfMeasureResponseList(unitOfMeasureServicePort.findAll(enterpriseId));
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
                                                                .create(unitOfMeasureRestMapper.toUnitOfMeasure(
                                                                                unitOfMeasureCreateRequest))));
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

        @Operation(summary = "Insertar las unidades de medida por defecto", description = "Este endpoint inserta las unidades de medida por defecto solo una vez. No puede ejecutarse nuevamente una vez que las unidades han sido insertadas.", responses = {
                        @ApiResponse(responseCode = "200", description = "Unidades de medida por defecto insertadas correctamente", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "400", description = "Las unidades de medida ya han sido insertadas", content = @Content(mediaType = "application/json")),
                        @ApiResponse(responseCode = "500", description = "Error al insertar las unidades de medida por defecto", content = @Content(mediaType = "application/json"))
        })
        @PostMapping("/insertDefaults")
        public ResponseEntity<String> insertDefaultUnits() {
                if (unitsInserted) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                        .body("Las unidades de medida ya han sido insertadas.");
                }

                // Definir las unidades de medida predeterminadas
                List<UnitOfMeasureCreateRequest> defaultUnits = List.of(
                                new UnitOfMeasureCreateRequest("U", "Unidad General", "standart", "Unidad General",
                                                "true"),
                                new UnitOfMeasureCreateRequest("kg", "Kilogramos", "standart", "Kilogramos", "true"),
                                new UnitOfMeasureCreateRequest("g", "Gramos", "standart", "Gramos", "true"),
                                new UnitOfMeasureCreateRequest("mg", "Miligramos", "standart", "Miligramos", "true"),
                                new UnitOfMeasureCreateRequest("lb", "Libras", "standart", "Libras", "true"),
                                new UnitOfMeasureCreateRequest("oz", "Onzas", "standart", "Onzas", "true"),
                                new UnitOfMeasureCreateRequest("l", "Litros", "standart", "Litros", "true"),
                                new UnitOfMeasureCreateRequest("ml", "Mililitros", "standart", "Mililitros", "true"),
                                new UnitOfMeasureCreateRequest("m³", "Metros cúbicos", "standart", "Metros cúbicos",
                                                "true"),
                                new UnitOfMeasureCreateRequest("cm³", "Centímetros cúbicos", "standart",
                                                "Centímetros cúbicos", "true"),
                                new UnitOfMeasureCreateRequest("gal", "Galones", "standart", "Galones", "true"),
                                new UnitOfMeasureCreateRequest("fl_oz", "Onzas fluidas", "standart", "Onzas fluidas",
                                                "true"),
                                new UnitOfMeasureCreateRequest("m", "Metros", "standart", "Metros", "true"),
                                new UnitOfMeasureCreateRequest("cm", "Centímetros", "standart", "Centímetros", "true"),
                                new UnitOfMeasureCreateRequest("mm", "Milímetros", "standart", "Milímetros", "true"),
                                new UnitOfMeasureCreateRequest("km", "Kilómetros", "standart", "Kilómetros", "true"),
                                new UnitOfMeasureCreateRequest("in", "Pulgadas", "standart", "Pulgadas", "true"),
                                new UnitOfMeasureCreateRequest("ft", "Pies", "standart", "Pies", "true"),
                                new UnitOfMeasureCreateRequest("yd", "Yardas", "standart", "Yardas", "true"),
                                new UnitOfMeasureCreateRequest("mi", "Millas", "standart", "Millas", "true"),
                                new UnitOfMeasureCreateRequest("m²", "Metros cuadrados", "standart", "Metros cuadrados",
                                                "true"),
                                new UnitOfMeasureCreateRequest("cm²", "Centímetros cuadrados", "standart",
                                                "Centímetros cuadrados", "true"),
                                new UnitOfMeasureCreateRequest("km²", "Kilómetros cuadrados", "standart",
                                                "Kilómetros cuadrados", "true"),
                                new UnitOfMeasureCreateRequest("in²", "Pulgadas cuadradas", "standart",
                                                "Pulgadas cuadradas", "true"),
                                new UnitOfMeasureCreateRequest("ft²", "Pies cuadrados", "standart", "Pies cuadrados",
                                                "true"),
                                new UnitOfMeasureCreateRequest("yd²", "Yardas cuadradas", "standart",
                                                "Yardas cuadradas", "true"),
                                new UnitOfMeasureCreateRequest("mi²", "Millas cuadradas", "standart",
                                                "Millas cuadradas", "true"),
                                new UnitOfMeasureCreateRequest("m³", "Metros cúbicos", "standart", "Metros cúbicos",
                                                "true"),
                                new UnitOfMeasureCreateRequest("cm³", "Centímetros cúbicos", "standart",
                                                "Centímetros cúbicos", "true"),
                                new UnitOfMeasureCreateRequest("km³", "Kilómetros cúbicos", "standart",
                                                "Kilómetros cúbicos", "true"),
                                new UnitOfMeasureCreateRequest("in³", "Pulgadas cúbicas", "standart",
                                                "Pulgadas cúbicas", "true"),
                                new UnitOfMeasureCreateRequest("ft³", "Pies cúbicos", "standart", "Pies cúbicos",
                                                "true"),
                                new UnitOfMeasureCreateRequest("yd³", "Yardas cúbicas", "standart", "Yardas cúbicas",
                                                "true"),
                                new UnitOfMeasureCreateRequest("mi³", "Millas cúbicas", "standart", "Millas cúbicas",
                                                "true"));
                // Insertar las unidades predeterminadas
                defaultUnits.forEach(
                                unit -> unitOfMeasureServicePort.create(unitOfMeasureRestMapper.toUnitOfMeasure(unit)));

                // Marcar como insertadas
                unitsInserted = true;

                return ResponseEntity.status(HttpStatus.OK)
                                .body("Unidades de medida predeterminadas insertadas exitosamente.");
        }

}
