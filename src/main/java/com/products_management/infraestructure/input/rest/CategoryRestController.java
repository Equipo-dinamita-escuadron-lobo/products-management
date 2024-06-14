package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.ICategoryServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.CategoryRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.CategoryCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.CategoryResponse;
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

    /**
     * Método para obtener todas las categorías de una empresa.
     * @param enterpriseId Identificador de la empresa.
     * @return Lista de respuestas de categorías.
     */
    @GetMapping("/findAll/{enterpriseId}")
    public List<CategoryResponse> findAll(@PathVariable String enterpriseId) {
        return categoryRestMapper.toCategoryResponseList(categoryServicePort.findAll(enterpriseId));
    }

    /**
     * Método para encontrar una categoría por su identificador.
     * @param id Identificador de la categoría.
     * @return Respuesta de la categoría encontrada.
     */
    @GetMapping("/findById/{id}")
    public CategoryResponse findById(@PathVariable Long id) {
        return categoryRestMapper.toCategoryResponse(categoryServicePort.findById(id));
    }

    /**
     * Método para obtener todas las categorías activadas de una empresa.
     * @param enterpriseId Identificador de la empresa.
     * @return Lista de respuestas de categorías activadas.
     */
    @GetMapping("/findActivate/{enterpriseId}")
    public List<CategoryResponse> findActivate(@PathVariable String enterpriseId) {
        return categoryRestMapper.toCategoryResponseList(categoryServicePort.findActivated(enterpriseId));
    }

    /**
     * Método para crear una nueva categoría.
     * @param categoryCreateRequest Datos de la categoría a crear.
     * @return Respuesta de la categoría creada con estado HTTP 201.
     */
    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> create(@Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(categoryRestMapper.toCategoryResponse(
                        categoryServicePort.create(categoryRestMapper.toCategory(categoryCreateRequest))));
    }

    /**
     * Método para actualizar una categoría existente.
     * @param id Identificador de la categoría a actualizar.
     * @param categoryCreateRequest Datos actualizados de la categoría.
     * @return Respuesta de la categoría actualizada.
     */
    @PutMapping("/update/{id}")
    public CategoryResponse update(@PathVariable Long id, @Valid @RequestBody CategoryCreateRequest categoryCreateRequest) {
        return categoryRestMapper.toCategoryResponse(
                categoryServicePort.update(id, categoryRestMapper.toCategory(categoryCreateRequest)));
    }

    /**
     * Método para cambiar el estado de activación de una categoría.
     * @param id Identificador de la categoría a cambiar de estado.
     */
    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        categoryServicePort.changeState(id);
    }

    /**
     * Método para eliminar una categoría por su identificador.
     * @param id Identificador de la categoría a eliminar.
     */
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        categoryServicePort.deleteById(id);
    }

    /**
     * Método para eliminar todas las categorías.
     */
    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        categoryServicePort.deleteAll();
    }
}
