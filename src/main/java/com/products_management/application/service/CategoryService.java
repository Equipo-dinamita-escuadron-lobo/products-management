package com.products_management.application.service;

import com.products_management.application.ports.input.ICategoryServicePort;
import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.domain.exception.CategoryAssociatedException;
import com.products_management.domain.exception.CategoryNotFoundException;
import com.products_management.domain.model.Category;
import com.products_management.domain.model.Product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import io.swagger.v3.oas.annotations.Operation; 
import io.swagger.v3.oas.annotations.Parameter; 
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Servicio que implementa la lógica de negocio para las categorías.
 * Esta clase interactúa con los puertos de persistencia y realiza las operaciones
 * necesarias para gestionar las categorías.
 */
@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryServicePort {

    private final ICategoryPersistencePort categoryPersistencePort;
    private final ProductService productServicePort;

    /**
     * Busca una categoría por su ID.
     *
     * @param id el ID de la categoría a buscar.
     * @return la categoría encontrada.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     */
    @Operation(summary = "Find a category by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Category found"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @Parameter(name = "id", description = "Category ID", required = true)
    @Override
    public Category findById(Long id) {
        return categoryPersistencePort.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * Obtiene una lista de todas las categorías asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las categorías de la empresa.
     */
    @Operation(summary = "Find all categories by enterprise ID", responses = {
        @ApiResponse(responseCode = "200", description = "Categories found")
    })
    @Parameter(name = "enterpriseId", description = "Enterprise ID", required = true)
    @Override
    public List<Category> findAll(String enterpriseId) {
        List<Category> allCategories = categoryPersistencePort.findAll();
        return allCategories.stream()
                .filter(category -> category.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todas las categorías activadas asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las categorías activadas de la empresa.
     */
    @Operation(summary = "Find all activated categories by enterprise ID", responses = {
        @ApiResponse(responseCode = "200", description = "Categories found")    
    })
    @Parameter(name = "enterpriseId", description = "Enterprise ID", required = true)
    @Override
    public List<Category> findActivated(String enterpriseId) {
        List<Category> allCategories = categoryPersistencePort.findAll();
        return allCategories.stream()
                .filter(category -> "true".equals(category.getState()))
                .filter(category -> category.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }

    /**
     * Crea una nueva categoría.
     *
     * @param category la categoría a crear.
     * @return la categoría creada.
     */
    @Operation(summary = "Create a new category", responses = {
        @ApiResponse(responseCode = "200", description = "Category created")
    })
    @Parameter(name = "category", description = "Category to create", required = true)
    @Override
    public Category create(Category category) {
        return categoryPersistencePort.create(category);
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param id el ID de la categoría a actualizar.
     * @param category los datos de la categoría actualizada.
     * @return la categoría actualizada.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     */
    @Operation(summary = "Update an existing category", responses = {
        @ApiResponse(responseCode = "200", description = "Category updated"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @Parameter(name = "id", description = "Category ID", required = true)
    @Parameter(name = "category", description = "Category to update", required = true)
    @Override
    public Category update(Long id, Category category) {
        return categoryPersistencePort.findById(id)
                .map(existingCategory -> {
                    existingCategory.setName(category.getName());
                    existingCategory.setDescription(category.getDescription());
                    existingCategory.setEnterpriseId(category.getEnterpriseId());
                    existingCategory.setInventoryId(category.getInventoryId());
                    existingCategory.setCostId(category.getCostId());
                    existingCategory.setSaleId(category.getSaleId());
                    existingCategory.setReturnId(category.getReturnId());
                    existingCategory.setTaxId(category.getTaxId());
                    return categoryPersistencePort.create(existingCategory);
                })
                .orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * Cambia el estado de una categoría (activado/desactivado).
     *
     * @param id el ID de la categoría cuyo estado se va a cambiar.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     */
    @Operation(summary = "Change the state of a category", responses = {
        @ApiResponse(responseCode = "200", description = "Category state changed"),
        @ApiResponse(responseCode = "404", description = "Category not found")
    })
    @Parameter(name = "id", description = "Category ID", required = true)
    @Override
    public void changeState(Long id) {
        Category category = categoryPersistencePort.findById(id)
                .orElseThrow(CategoryNotFoundException::new);
        category.setState("true".equals(category.getState()) ? "false" : "true");
        categoryPersistencePort.create(category);
    }

    /**
     * Elimina una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     * @throws CategoryAssociatedException si la categoría está asociada a productos.
     */
    @Operation(summary = "Delete a category by ID", responses = {
        @ApiResponse(responseCode = "200", description = "Category deleted"),
        @ApiResponse(responseCode = "404", description = "Category not found"),
        @ApiResponse(responseCode = "409", description = "Category associated")
    })
    @Parameter(name = "id", description = "Category ID", required = true)
    @Override
    public void deleteById(Long id) {
        if (categoryPersistencePort.findById(id).isEmpty()) {
            throw new CategoryNotFoundException();
        }
        List<Product> products = productServicePort.findAllByCategory(id);
        if (!products.isEmpty()) {
            throw new CategoryAssociatedException();
        }
        categoryPersistencePort.deleteById(id);
    }

    /**
     * Elimina todas las categorías.
     */
    @Operation(summary = "Delete all categories", responses = {
        @ApiResponse(responseCode = "200", description = "Categories deleted")
    })
    @Override
    public void deleteAll() {
        categoryPersistencePort.deleteAll();
    }
}
