package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.ProductRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para la gestión de productos.
 */
@CrossOrigin("*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductRestController {

    private final IProductServicePort productServicePort;
    private final ProductRestMapperImpl productRestMapper;

    /**
     * Método para obtener todos los productos de una empresa.
     * @param enterpriseId Identificador de la empresa.
     * @return Lista de respuestas de productos.
     */
    @GetMapping("/findAll/{enterpriseId}")
    public List<ProductResponse> findAll(@PathVariable String enterpriseId) {
        return productRestMapper.toProductResponseList(productServicePort.findAll(enterpriseId));
    }

    /**
     * Método para obtener todos los productos por unidad de medida.
     * @param unitOfMeasureId Identificador de la unidad de medida.
     * @return Lista de respuestas de productos.
     */
    @GetMapping("/findAllByUnitOfMeasure/{unitOfMeasureId}")
    public List<ProductResponse> findAllByUnitOfMeasure(@PathVariable Long unitOfMeasureId) {
        return productRestMapper.toProductResponseList(productServicePort.findAllByUnitOfMeasure(unitOfMeasureId));
    }

    /**
     * Método para obtener todos los productos por categoría.
     * @param categoryId Identificador de la categoría.
     * @return Lista de respuestas de productos.
     */
    @GetMapping("/findAllByCategoryId/{categoryId}")
    public List<ProductResponse> findAllByCategory(@PathVariable Long categoryId) {
        return productRestMapper.toProductResponseList(productServicePort.findAllByCategory(categoryId));
    }

    /**
     * Método para encontrar un producto por su identificador.
     * @param id Identificador del producto.
     * @return Respuesta del producto encontrado.
     */
    @GetMapping("/findById/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productRestMapper.toProductResponse(productServicePort.findById(id));
    }

    /**
     * Método para obtener todos los productos activados de una empresa.
     * @param enterpriseId Identificador de la empresa.
     * @return Lista de respuestas de productos activados.
     */
    @GetMapping("/findActivate/{enterpriseId}")
    public List<ProductResponse> findActivate(@PathVariable String enterpriseId) {
        return productRestMapper.toProductResponseList(productServicePort.findActivated(enterpriseId));
    }

    /**
     * Método para crear un nuevo producto.
     * @param productCreateRequest Datos del producto a crear.
     * @return Respuesta del producto creado con estado HTTP 201.
     */
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productRestMapper.toProductResponse(
                        productServicePort.create(productRestMapper.toProduct(productCreateRequest))));
    }

    /**
     * Método para actualizar un producto existente.
     * @param id Identificador del producto a actualizar.
     * @param productCreateRequest Datos actualizados del producto.
     * @return Respuesta del producto actualizado.
     */
    @PutMapping("/update/{id}")
    public ProductResponse update(@PathVariable Long id, @Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return productRestMapper.toProductResponse(
                productServicePort.update(id, productRestMapper.toProduct(productCreateRequest)));
    }

    /**
     * Método para cambiar el estado de activación de un producto.
     * @param id Identificador del producto a cambiar de estado.
     */
    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        productServicePort.changeState(id);
    }

    /**
     * Método para eliminar un producto por su identificador.
     * @param id Identificador del producto a eliminar.
     */
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        productServicePort.deleteById(id);
    }

    /**
     * Método para eliminar todos los productos.
     */
    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        productServicePort.deleteAll();
    }

}
