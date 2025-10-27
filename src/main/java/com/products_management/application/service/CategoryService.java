package com.products_management.application.service;

import com.products_management.application.ports.input.ICategoryServicePort;
import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.domain.exception.category.CategoryAssociatedException;
import com.products_management.domain.exception.category.CategoryNotFoundException;
import com.products_management.domain.exception.category.CategoryNameAlreadyExistsException;
import com.products_management.domain.model.Category;
import com.products_management.domain.model.Product;
import com.products_management.domain.utils.StringNormalizer;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


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
     * Busca una categoría por su ID y empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría a buscar.
     * @return la categoría encontrada.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     */

    @Override
    public Category findById(String enterpriseId, Long id) {
        return categoryPersistencePort.findByIdAndEnterpriseId(id, enterpriseId).orElseThrow(CategoryNotFoundException::new);
    }

    /**
     * Obtiene una lista de todas las categorías asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las categorías de la empresa.
     */

    @Override
    public List<Category> findAll(String enterpriseId) {
        return categoryPersistencePort.findByEnterpriseId(enterpriseId);
    }

    /**
     * Obtiene una lista de todas las categorías activadas asociadas a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todas las categorías activadas de la empresa.
     */

    @Override
    public List<Category> findActivated(String enterpriseId) {
        return categoryPersistencePort.findByEnterpriseIdAndState(enterpriseId, true);
    }

    /**
     * Crea una nueva categoría.
     *
     * @param category la categoría a crear.
     * @return la categoría creada.
     * @throws CategoryNameAlreadyExistsException si ya existe una categoría con el mismo nombre.
     */

    @Override
    public Category create(Category category) {
        // Normalizar el nombre de manera consistente (para validación y almacenamiento)
        category.setName(StringNormalizer.normalize(category.getName()));
        validateCategoryUniqueness(category);
        return categoryPersistencePort.create(category);
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría a actualizar.
     * @param category los datos de la categoría actualizada.
     * @return la categoría actualizada.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     * @throws CategoryNameAlreadyExistsException si ya existe una categoría con el mismo nombre.
     */

    @Override
    public Category update(String enterpriseId, Long id, Category category) {
        return categoryPersistencePort.findByIdAndEnterpriseId(id, enterpriseId)
                .map(existingCategory -> {
                    // Normalizar el nombre de manera consistente (para validación y almacenamiento)
                    category.setName(StringNormalizer.normalize(category.getName()));
                    validateCategoryUniquenessForUpdate(id, category);
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
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría cuyo estado se va a cambiar.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     */

    @Override
    public void changeState(String enterpriseId, Long id) {
        Category category = categoryPersistencePort.findByIdAndEnterpriseId(id, enterpriseId)
                .orElseThrow(CategoryNotFoundException::new);
        category.setState(!category.isState());
        categoryPersistencePort.create(category);
    }

    /**
     * Elimina una categoría por su ID y empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @param id el ID de la categoría a eliminar.
     * @throws CategoryNotFoundException si la categoría no se encuentra.
     * @throws CategoryAssociatedException si la categoría está asociada a productos.
     */

    @Override
    public void deleteById(String enterpriseId, Long id) {
        if (categoryPersistencePort.findByIdAndEnterpriseId(id, enterpriseId).isEmpty()) {
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

    /**
     * Valida que el nombre de una categoría sea único dentro de la empresa.
     *
     * @param category la categoría a validar.
     * @throws CategoryNameAlreadyExistsException si ya existe una categoría con el mismo nombre.
     */
    private void validateCategoryUniqueness(Category category) {
        // El nombre ya está normalizado, se usa directamente para validación
        if (categoryPersistencePort.existsByNameAndEnterpriseId(
                category.getName(), category.getEnterpriseId())) {
            throw new CategoryNameAlreadyExistsException();
        }
    }

    /**
     * Valida que el nombre de una categoría sea único dentro de la empresa
     * durante una actualización, excluyendo la categoría que se está actualizando.
     *
     * @param id el ID de la categoría que se está actualizando.
     * @param category la categoría a validar.
     * @throws CategoryNameAlreadyExistsException si ya existe otra categoría con el mismo nombre.
     */
    private void validateCategoryUniquenessForUpdate(Long id, Category category) {
        // El nombre ya está normalizado, se usa directamente para validación
        if (categoryPersistencePort.existsByNameAndEnterpriseIdAndIdNot(
                category.getName(), category.getEnterpriseId(), id)) {
            throw new CategoryNameAlreadyExistsException();
        }
    }
}
