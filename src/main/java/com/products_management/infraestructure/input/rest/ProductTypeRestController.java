package com.products_management.infraestructure.input.rest;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductTypeRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductTypeRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductTypeResponse;

import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import com.products_management.application.ports.input.IProductTypeServicePort;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de tipos de producto.
 */
@CrossOrigin("*") // Allow cross-origin requests
@RestController
@RequestMapping("/api/product-types")
@RequiredArgsConstructor // Use constructor injection
public class ProductTypeRestController {

    private final IProductTypeServicePort productTypeService;
    private final IProductTypeRestMapper productTypeMapper;

    @Operation(summary = "Crear un nuevo tipo de producto", description = "Este endpoint permite crear un nuevo tipo de producto utilizando los datos proporcionados en el cuerpo de la solicitud.", responses = {
            @ApiResponse(responseCode = "201", description = "Tipo de producto creado exitosamente", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida, los datos proporcionados no son correctos", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al crear el tipo de producto", content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<ProductTypeResponse> createProductType(@RequestBody ProductTypeRequest productTypeRequest) {
        ProductType productType = productTypeMapper.toProductType(productTypeRequest);
        ProductType createdProductType = productTypeService.createProductType(productType);
        ProductTypeResponse response = productTypeMapper.toProductTypeResponse(createdProductType);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Obtener todos los tipos de productos", description = "Este endpoint permite obtener una lista de todos los tipos de productos disponibles en el sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de productos obtenida con éxito", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener los tipos de productos", content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<List<ProductTypeResponse>> getAllProductTypes() {
        List<ProductType> productTypes = productTypeService.listAllProductTypes();
        List<ProductTypeResponse> responses = productTypes.stream()
                .map(productTypeMapper::toProductTypeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtener tipos de productos por ID de empresa", description = "Este endpoint permite obtener una lista de tipos de productos asociados a una empresa específica.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de tipos de productos obtenida con éxito para la empresa especificada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Empresa no encontrada", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "500", description = "Error interno al obtener los tipos de productos", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/enterprise/{enterpriseId}")
    public ResponseEntity<List<ProductTypeResponse>> getProductTypesByEnterpriseId(@PathVariable String enterpriseId) {
        List<ProductType> productTypes = productTypeService.getProductTypesByEnterpriseId(enterpriseId);
        List<ProductTypeResponse> responses = productTypes.stream()
                .map(productTypeMapper::toProductTypeResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @Operation(summary = "Obtener un tipo de producto por su identificador", description = "Recupera un tipo de producto específico utilizando el identificador proporcionado.", responses = {
            @ApiResponse(responseCode = "200", description = "Tipo de producto encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado", content = @Content(mediaType = "application/json"))
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> getProductTypeById(@PathVariable Long id) {
        Optional<ProductType> productTypeOptional = productTypeService.findById(id);
        if (productTypeOptional.isPresent()) {
            ProductTypeResponse response = productTypeMapper.toProductTypeResponse(productTypeOptional.get());
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(summary = "Actualizar un tipo de producto", description = "Permite actualizar un tipo de producto existente utilizando su identificador y los datos proporcionados.", responses = {
            @ApiResponse(responseCode = "200", description = "Tipo de producto actualizado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta o datos inválidos", content = @Content(mediaType = "application/json"))
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductTypeResponse> updateProductType(
            @PathVariable Long id,
            @RequestBody ProductTypeRequest productTypeRequest) {
        ProductType productType = productTypeMapper.toProductType(productTypeRequest);
        ProductType updatedProductType = productTypeService.updateProductType(id, productType);
        ProductTypeResponse response = productTypeMapper.toProductTypeResponse(updatedProductType);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Eliminar un tipo de producto", description = "Permite eliminar un tipo de producto utilizando su identificador.", responses = {
            @ApiResponse(responseCode = "204", description = "Tipo de producto eliminado exitosamente", content = @Content(mediaType = "application/json")),
            @ApiResponse(responseCode = "404", description = "Tipo de producto no encontrado", content = @Content(mediaType = "application/json"))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        productTypeService.deleteProductType(id);
        return ResponseEntity.noContent().build();
    }
}
