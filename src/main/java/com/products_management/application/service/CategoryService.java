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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * Valida que el nombre de una categoría sea único dentro de la empresa.
     *
     * @param category la categoría a validar.
     * @throws CategoryNameAlreadyExistsException si ya existe una categoría con el mismo nombre.
     */
    private void validateCategoryUniqueness(Category category) {
        // El nombre ya está normalizado, se usa directamente para validación
        if (categoryPersistencePort.existsByNameAndEnterpriseId(
                category.getName(), category.getEnterpriseId())) {
            throw new CategoryNameAlreadyExistsException(category.getName());
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
            throw new CategoryNameAlreadyExistsException(category.getName());
        }
    }

    /**
     * Obtiene todas las categorías de una empresa con paginación.
     *
     * @param enterpriseId el ID de la empresa
     * @param pageable información de paginación
     * @return página de categorías encontradas (puede estar vacía si no hay datos)
     */
    @Override
    public Page<Category> getAllCategoriesBy(String enterpriseId, Pageable pageable) {
        return categoryPersistencePort.getAllCategoriesBy(enterpriseId, pageable);
    }

    /**
     * Cuenta el total de categorías por empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return el número total de categorías
     */
    @Override
    public long countAllCategoriesByEntId(String enterpriseId) {
        return categoryPersistencePort.countByEnterpriseId(enterpriseId);
    }

    /**
     * Busca categorías por empresa y término de búsqueda con ordenamiento.
     *
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías que coinciden con la búsqueda
     */
    @Override
    public Page<Category> findByEntIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder) {
        return categoryPersistencePort.findByEnterpriseIdAndSearch(enterpriseId, search, Pageable.ofSize(size).withPage(page));
    }

    /**
     * Cuenta categorías por empresa y término de búsqueda.
     *
     * @param enterpriseId El id de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de categorías que coinciden
     */
    @Override
    public long countByEntIdAndSearch(String enterpriseId, String search) {
        return categoryPersistencePort.countByEnterpriseIdAndSearch(enterpriseId, search);
    }

    /**
     * Obtiene todas las categorías con ordenamiento.
     *
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías ordenadas
     */
    @Override
    public Page<Category> getAllCategoriesByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        return categoryPersistencePort.getAllCategoriesByWithSort(enterpriseId, page, size, sortField, sortOrder);
    }

    /**
     * Obtiene todas las categorías activas con ordenamiento.
     *
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías activas ordenadas
     */
    @Override
    public Page<Category> getAllActiveCategoriesByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        return categoryPersistencePort.getActiveCategoriesBy(enterpriseId, page, size, sortField, sortOrder);
    }

    /**
     * Cuenta el total de categorías activas por empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return el número total de categorías activas
     */
    @Override
    public long countActiveCategoriesByEntId(String enterpriseId) {
        return categoryPersistencePort.countActiveByEnterpriseId(enterpriseId);
    }
}
