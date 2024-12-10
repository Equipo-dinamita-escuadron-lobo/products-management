package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.ICategoryServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.CategoryRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;

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
 * Controlador REST para la gestión de categorías de productos.
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/categories")
public class CategoryRestController {

    private final ICategoryServicePort categoryServicePort;
    private final CategoryRestMapperImpl categoryRestMapper;

    @Operation(summary = "Obtener todas las categorías de una empresa", description = "Este endpoint permite obtener todas las categorías asociadas a una empresa identificada por su ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Categorías obtenidas con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findAll/{enterpriseId}")
    public List<CategoryResponse> findAll(@PathVariable String enterpriseId) {
        return categoryRestMapper.toCategoryResponseList(categoryServicePort.findAll(enterpriseId));
    }

    @Operation(summary = "Obtener una categoría por su identificador", description = "Este endpoint permite obtener una categoría específica utilizando su identificador único.", responses = {
            @ApiResponse(responseCode = "200", description = "Categoría encontrada con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findById/{id}")
    public CategoryResponse findById(@PathVariable Long id) {
        return categoryRestMapper.toCategoryResponse(categoryServicePort.findById(id));
    }

    @Operation(summary = "Obtener todas las categorías activadas de una empresa", description = "Este endpoint permite obtener todas las categorías activadas de una empresa específica utilizando su identificador único.", responses = {
            @ApiResponse(responseCode = "200", description = "Categorías activadas encontradas con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No se encontraron categorías activadas", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findActivate/{enterpriseId}")
    public List<CategoryResponse> findActivate(@PathVariable String enterpriseId) {
        return categoryRestMapper.toCategoryResponseList(categoryServicePort.findActivated(enterpriseId));
    }

    @Operation(summary = "Crear una nueva categoría", description = "Este endpoint permite crear una nueva categoría en el sistema utilizando los datos proporcionados en la solicitud.", responses = {
            @ApiResponse(responseCode = "201", description = "Categoría creada con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryRestMapper.toCategoryResponse(
                        categoryServicePort.create(categoryRestMapper.toCategory(categoryCreateRequest))));
    }

    @Operation(summary = "Actualizar una categoría existente", description = "Este endpoint permite actualizar una categoría existente en el sistema utilizando los datos proporcionados en la solicitud.", responses = {
            @ApiResponse(responseCode = "200", description = "Categoría actualizada con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/update/{id}")
    public CategoryResponse update(@PathVariable Long id,
            @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return categoryRestMapper.toCategoryResponse(
                categoryServicePort.update(id, categoryRestMapper.toCategory(categoryCreateRequest)));
    }

    @Operation(summary = "Cambiar el estado de activación de una categoría", description = "Este endpoint permite cambiar el estado de activación de una categoría mediante su identificador.", responses = {
            @ApiResponse(responseCode = "200", description = "Estado de la categoría actualizado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        categoryServicePort.changeState(id);
    }

    @Operation(summary = "Eliminar una categoría por su identificador", description = "Este endpoint permite eliminar una categoría de la base de datos mediante su identificador.", responses = {
            @ApiResponse(responseCode = "200", description = "Categoría eliminada con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Categoría no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryServicePort.deleteById(id);
    }

    @Operation(summary = "Eliminar todas las categorías", description = "Este endpoint permite eliminar todas las categorías de la base de datos.", responses = {
            @ApiResponse(responseCode = "200", description = "Todas las categorías eliminadas con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        categoryServicePort.deleteAll();
    }
}
