package com.products_management.infraestructure.input.rest;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.infraestructure.input.rest.mapper.impl.ProductRestMapperImpl;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

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

    @Operation(summary = "Obtener todos los productos de una empresa", description = "Este endpoint permite obtener todos los productos asociados a una empresa, proporcionando el identificador de la empresa.", responses = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener los productos", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findAll/{enterpriseId}")
    public List<ProductResponse> findAll(@PathVariable String enterpriseId) {
        return productRestMapper.toProductResponseList(productServicePort.findAll(enterpriseId));
    }

    @Operation(summary = "Obtener todos los productos por unidad de medida", description = "Este endpoint permite obtener todos los productos asociados a una unidad de medida específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener los productos", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findAllByUnitOfMeasure/{unitOfMeasureId}")
    public List<ProductResponse> findAllByUnitOfMeasure(@PathVariable Long unitOfMeasureId) {
        return productRestMapper.toProductResponseList(productServicePort.findAllByUnitOfMeasure(unitOfMeasureId));
    }

    @Operation(summary = "Obtener todos los productos por categoría", description = "Este endpoint permite obtener todos los productos asociados a una categoría específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Productos obtenidos con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener los productos", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findAllByCategoryId/{categoryId}")
    public List<ProductResponse> findAllByCategory(@PathVariable Long categoryId) {
        return productRestMapper.toProductResponseList(productServicePort.findAllByCategory(categoryId));
    }

    @Operation(summary = "Obtener un producto por su identificador", description = "Este endpoint permite obtener los detalles de un producto específico mediante su identificador.", responses = {
            @ApiResponse(responseCode = "200", description = "Producto encontrado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener el producto", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findById/{id}")
    public ProductResponse findById(@PathVariable Long id) {
        return productRestMapper.toProductResponse(productServicePort.findById(id));
    }

    @Operation(summary = "Obtener todos los productos activados de una empresa", description = "Este endpoint permite obtener una lista de todos los productos que están activados en una empresa específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Productos activados encontrados con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "No se encontraron productos activados", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener los productos activados", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/findActivate/{enterpriseId}")
    public List<ProductResponse> findActivate(@PathVariable String enterpriseId) {
        return productRestMapper.toProductResponseList(productServicePort.findActivated(enterpriseId));
    }

    @Operation(summary = "Crear un nuevo producto", description = "Este endpoint permite crear un nuevo producto en el sistema utilizando los datos proporcionados en el cuerpo de la solicitud.", responses = {
            @ApiResponse(responseCode = "201", description = "Producto creado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del producto no válidos", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al crear el producto", content = @Content(mediaType = "application/json"))
    })
    @PostMapping("/create")
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productRestMapper.toProductResponse(
                        productServicePort.create(productRestMapper.toProduct(productCreateRequest))));
    }

    @Operation(summary = "Actualizar un producto existente", description = "Este endpoint permite actualizar los datos de un producto existente en el sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "Producto actualizado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, datos del producto no válidos", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al actualizar el producto", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/update/{id}")
    public ProductResponse update(@PathVariable Long id,
            @Valid @RequestBody ProductCreateRequest productCreateRequest) {
        return productRestMapper.toProductResponse(
                productServicePort.update(id, productRestMapper.toProduct(productCreateRequest)));
    }

    @Operation(summary = "Cambiar el estado de activación de un producto", description = "Este endpoint permite cambiar el estado de activación de un producto, activándolo o desactivándolo.", responses = {
            @ApiResponse(responseCode = "200", description = "Estado de activación del producto actualizado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al cambiar el estado del producto", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/changeState/{id}")
    public void changeState(@PathVariable Long id) {
        productServicePort.changeState(id);
    }

    @Operation(summary = "Eliminar un producto por su identificador", description = "Este endpoint permite eliminar un producto utilizando su identificador único.", responses = {
            @ApiResponse(responseCode = "200", description = "Producto eliminado con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Producto no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar el producto", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/delete/{id}")
    public void deleteById(@PathVariable Long id) {
        productServicePort.deleteById(id);
    }

    @Operation(summary = "Eliminar todos los productos", description = "Este endpoint permite eliminar todos los productos de la empresa.", responses = {
            @ApiResponse(responseCode = "200", description = "Productos eliminados con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al eliminar los productos", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/deleteAll")
    public void deleteAll() {
        productServicePort.deleteAll();
    }

}
