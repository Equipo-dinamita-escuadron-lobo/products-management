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
    @Override
    public void deleteAll() {
        categoryPersistencePort.deleteAll();
    }
}
