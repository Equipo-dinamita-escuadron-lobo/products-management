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
    private static boolean unitsInserted = false; 

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


    /**
     * Método para insertar las unidades de medida solo una vez.
     * Este método solo puede ser ejecutado una vez, asegurando que no se inserten nuevamente.
     * @return Respuesta con el estado HTTP 200 si se realizó correctamente.
     */
    @PostMapping("/insertDefaults")
    public ResponseEntity<String> insertDefaultUnits() {
        if (unitsInserted) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Las unidades de medida ya han sido insertadas.");
        }

        // Definir las unidades de medida predeterminadas
        List<UnitOfMeasureCreateRequest> defaultUnits = List.of(
            new UnitOfMeasureCreateRequest("U", "Unidad General", "standart", "Unidad General", "true"),
            new UnitOfMeasureCreateRequest("kg", "Kilogramos", "standart", "Kilogramos", "true"),
            new UnitOfMeasureCreateRequest("g", "Gramos", "standart", "Gramos", "true"),
            new UnitOfMeasureCreateRequest("mg", "Miligramos", "standart", "Miligramos", "true"),
            new UnitOfMeasureCreateRequest("lb", "Libras", "standart", "Libras", "true"),
            new UnitOfMeasureCreateRequest("oz", "Onzas", "standart", "Onzas", "true"),
            new UnitOfMeasureCreateRequest("l", "Litros", "standart", "Litros", "true"),
            new UnitOfMeasureCreateRequest("ml", "Mililitros", "standart", "Mililitros", "true"),
            new UnitOfMeasureCreateRequest("m³", "Metros cúbicos", "standart", "Metros cúbicos", "true"),
            new UnitOfMeasureCreateRequest("cm³", "Centímetros cúbicos", "standart", "Centímetros cúbicos", "true"),
            new UnitOfMeasureCreateRequest("gal", "Galones", "standart", "Galones", "true"),
            new UnitOfMeasureCreateRequest("fl_oz", "Onzas fluidas", "standart", "Onzas fluidas", "true"),
            new UnitOfMeasureCreateRequest("m", "Metros", "standart", "Metros", "true"),
            new UnitOfMeasureCreateRequest("cm", "Centímetros", "standart", "Centímetros", "true"),
            new UnitOfMeasureCreateRequest("mm", "Milímetros", "standart", "Milímetros", "true"),
            new UnitOfMeasureCreateRequest("km", "Kilómetros", "standart", "Kilómetros", "true"),
            new UnitOfMeasureCreateRequest("in", "Pulgadas", "standart", "Pulgadas", "true"),
            new UnitOfMeasureCreateRequest("ft", "Pies", "standart", "Pies", "true"),
            new UnitOfMeasureCreateRequest("yd", "Yardas", "standart", "Yardas", "true"),
            new UnitOfMeasureCreateRequest("mi", "Millas", "standart", "Millas", "true"),
            new UnitOfMeasureCreateRequest("m²", "Metros cuadrados", "standart", "Metros cuadrados", "true"),
            new UnitOfMeasureCreateRequest("cm²", "Centímetros cuadrados", "standart", "Centímetros cuadrados", "true"),
            new UnitOfMeasureCreateRequest("km²", "Kilómetros cuadrados", "standart", "Kilómetros cuadrados", "true"),
            new UnitOfMeasureCreateRequest("in²", "Pulgadas cuadradas", "standart", "Pulgadas cuadradas", "true"),
            new UnitOfMeasureCreateRequest("ft²", "Pies cuadrados", "standart", "Pies cuadrados", "true"),
            new UnitOfMeasureCreateRequest("yd²", "Yardas cuadradas", "standart", "Yardas cuadradas", "true"),
            new UnitOfMeasureCreateRequest("mi²", "Millas cuadradas", "standart", "Millas cuadradas", "true"),
            new UnitOfMeasureCreateRequest("m³", "Metros cúbicos", "standart", "Metros cúbicos", "true"),
            new UnitOfMeasureCreateRequest("cm³", "Centímetros cúbicos", "standart", "Centímetros cúbicos", "true"),
            new UnitOfMeasureCreateRequest("km³", "Kilómetros cúbicos", "standart", "Kilómetros cúbicos", "true"),
            new UnitOfMeasureCreateRequest("in³", "Pulgadas cúbicas", "standart", "Pulgadas cúbicas", "true"),
            new UnitOfMeasureCreateRequest("ft³", "Pies cúbicos", "standart", "Pies cúbicos", "true"),
            new UnitOfMeasureCreateRequest("yd³", "Yardas cúbicas", "standart", "Yardas cúbicas", "true"),
            new UnitOfMeasureCreateRequest("mi³", "Millas cúbicas", "standart", "Millas cúbicas", "true")
        );
        // Insertar las unidades predeterminadas
        defaultUnits.forEach(unit -> unitOfMeasureServicePort.create(unitOfMeasureRestMapper.toUnitOfMeasure(unit)));

        // Marcar como insertadas
        unitsInserted = true;

        return ResponseEntity.status(HttpStatus.OK)
                .body("Unidades de medida predeterminadas insertadas exitosamente.");
    }

}
