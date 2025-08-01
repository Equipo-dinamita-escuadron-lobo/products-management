package com.products_management.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.products_management.application.dto.ProductSyncDto;
import com.products_management.application.ports.input.IProductEventPort;
import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.exception.ProductNotFoundException;
import com.products_management.domain.model.Product;

import lombok.RequiredArgsConstructor;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

/**
 * Servicio que implementa la lógica de negocio para los productos.
 * Esta clase interactúa con los puertos de persistencia y realiza las operaciones
 * necesarias para gestionar los productos.
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;
    private final IProductEventPort productEventPort;

    /**
     * Busca un producto por su ID.
     *
     * @param id el ID del producto a buscar.
     * @return el producto encontrado.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Operation(summary = "Busca un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto encontrado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Parameter(name = "id", description = "El ID del producto a buscar", required = true)
    @Override
    public Product findById(Long id) {
        return productPersistencePort.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Obtiene una lista de todos los productos asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los productos de la empresa.
     */
    @Operation(summary = "Obtiene una lista de todos los productos asociados a una empresa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada")
    })
    @Parameter(name = "enterpriseId", description = "El ID de la empresa", required = true)
    @Override
    public List<Product> findAll(String enterpriseId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> product.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los productos activados asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los productos activados de la empresa.
     */
    @Operation(summary = "Obtiene una lista de todos los productos activados asociados a una empresa")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos activados encontrada")
    })
    @Parameter(name = "enterpriseId", description = "El ID de la empresa", required = true)
    @Override
    public List<Product> findActivated(String enterpriseId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> "true".equals(product.getState()))
                .filter(product -> product.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo producto.
     *
     * @param product el producto a crear.
     * @return el producto creado.
     */
    @Operation(summary = "Crea un nuevo producto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto creado")
    })
    @Parameter(name = "product", description = "El producto a crear", required = true)
    @Override
    public Product create(Product product) {
        product.generateCode();
        Product createdProduct = productPersistencePort.create(product);
        
        ProductSyncDto productSyncDto = new ProductSyncDto(
            createdProduct.getId(),
            createdProduct.getName(),
            createdProduct.getReference(),
            createdProduct.getEnterpriseId(),
            createdProduct.getPresentation(),
            createdProduct.getQuantity(),
            createdProduct.getCost()
        );
        productEventPort.publishCreatedStockEvent(productSyncDto);
        return createdProduct;
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id el ID del producto a actualizar.
     * @param product los datos del producto actualizado.
     * @return el producto actualizado.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Operation(summary = "Actualiza un producto existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto actualizado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Parameter(name = "id", description = "El ID del producto a actualizar", required = true)
    @Parameter(name = "product", description = "Los datos del producto actualizado", required = true)
    @Override
    public Product update(Long id, Product product) {
        return productPersistencePort.findById(id)
            .map(existingProduct -> {
                boolean shouldRegenerateCode = !existingProduct.getName().equals(product.getName()) || 
                                               !existingProduct.getCategoryId().equals(product.getCategoryId()) || 
                                               !existingProduct.getId().equals(product.getId());

                existingProduct.setName(product.getName());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setQuantity(product.getQuantity());
                existingProduct.setTaxPercentage(product.getTaxPercentage());
                existingProduct.setUnitOfMeasureId(product.getUnitOfMeasureId());
                existingProduct.setCategoryId(product.getCategoryId());
                existingProduct.setCost(product.getCost());
                existingProduct.setProductTypeId(product.getProductTypeId());
                existingProduct.setReference(product.getReference());

                if (shouldRegenerateCode) {
                    existingProduct.generateCode();
                }

                return productPersistencePort.create(existingProduct);
            })
            .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Cambia el estado de un producto (activado/desactivado).
     *
     * @param id el ID del producto cuyo estado se va a cambiar.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Operation(summary = "Cambia el estado de un producto (activado/desactivado)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Estado del producto cambiado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Parameter(name = "id", description = "El ID del producto cuyo estado se va a cambiar", required = true)
    @Override
    public void changeState(Long id) {
        Product product = productPersistencePort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException());
        product.setState("true".equals(product.getState()) ? "false" : "true");
        productPersistencePort.create(product);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id el ID del producto a eliminar.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Operation(summary = "Elimina un producto por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Producto eliminado"),
        @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    })
    @Parameter(name = "id", description = "El ID del producto a eliminar", required = true)
    @Override
    public void deleteById(Long id) {
        if (productPersistencePort.findById(id).isEmpty()) {
            throw new ProductNotFoundException();
        }
        productPersistencePort.deleteById(id);
    }

    /**
     * Elimina todos los productos.
     */
    @Operation(summary = "Elimina todos los productos")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Productos eliminados")
    })
    @Override
    public void deleteAll() {
        productPersistencePort.deleteAll();
    }

    /**
     * Obtiene una lista de todos los productos asociados a una categoría.
     *
     * @param categoryId el ID de la categoría.
     * @return una lista de todos los productos de la categoría.
     */
    @Operation(summary = "Obtiene una lista de todos los productos asociados a una categoría")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada")
    })
    @Parameter(name = "categoryId", description = "El ID de la categoría", required = true)
    @Override
    public List<Product> findAllByCategory(Long categoryId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> product.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los productos asociados a una unidad de medida.
     *
     * @param unitOfMeasureId el ID de la unidad de medida.
     * @return una lista de todos los productos de la unidad de medida.
     */
    @Operation(summary = "Obtiene una lista de todos los productos asociados a una unidad de medida")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de productos encontrada")
    })
    @Parameter(name = "unitOfMeasureId", description = "El ID de la unidad de medida", required = true)
    @Override
    public List<Product> findAllByUnitOfMeasure(Long unitOfMeasureId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> product.getUnitOfMeasureId().equals(unitOfMeasureId))
                .collect(Collectors.toList());
    }
}
