package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.entity.CategoryEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.ICategoryPersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @brief Adaptador de persistencia para operaciones CRUD de categorías
 *
 * Implementa ICategoryPersistencePort para gestionar persistencia de categorías
 * con soporte para multitenancy por empresa y operaciones paginadas.
 */
@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryPersistenceMapper categoryPersistenceMapper;

    @Override
    public Optional<Category> findByIdAndEnterpriseId(Long id, String enterpriseId) {
        return categoryRepository.findByIdAndEnterpriseId(id, enterpriseId)
                .map(categoryPersistenceMapper::toCategory);
    }

    @Override
    public Category create(Category category) {
        CategoryEntity entity = categoryPersistenceMapper.toCategoryEntity(category);

        // Si es una actualización (tiene ID), asegurar que los taxes se actualicen correctamente
        if (category.getId() != null) {
            Optional<CategoryEntity> existingEntity = categoryRepository.findById(category.getId());
            if (existingEntity.isPresent()) {
                CategoryEntity existing = existingEntity.get();
                // Asegurar que los taxes se actualicen correctamente
                existing.setTaxes(category.getTaxes());
                entity = categoryRepository.save(existing);
            } else {
                entity = categoryRepository.save(entity);
            }
        } else {
            entity = categoryRepository.save(entity);
        }

        return categoryPersistenceMapper.toCategory(entity);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public boolean existsByNameAndEnterpriseId(String name, String enterpriseId) {
        return categoryRepository.existsByNameAndEnterpriseId(name, enterpriseId);
    }

    @Override
    public boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id) {
        return categoryRepository.existsByNameAndEnterpriseIdAndIdNot(name, enterpriseId, id);
    }

    @Override
    public Page<Category> getAllCategoriesBy(String enterpriseId, Pageable pageable) {
        Page<CategoryEntity> pageEntities = categoryRepository.getCategoriesBy(enterpriseId, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    @Override
    public Page<Category> getAllCategoriesByState(String enterpriseId, Boolean state, Pageable pageable) {
        Page<CategoryEntity> pageEntities = categoryRepository.getCategoriesByEnterpriseIdAndState(enterpriseId, state, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    @Override
    public Page<Category> findByEnterpriseIdAndSearch(String enterpriseId, String search, Pageable pageable) {
        Page<CategoryEntity> pageEntities = categoryRepository.findByEnterpriseIdAndSearch(enterpriseId, search, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    @Override
    public long countByEnterpriseIdAndSearch(String enterpriseId, String search) {
        return categoryRepository.countByEnterpriseIdAndSearch(enterpriseId, search);
    }

    @Override
    public Page<Category> getAllCategoriesByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        String entitySortField = mapCategorySortField(sortField);
        Sort sort = "desc".equalsIgnoreCase(sortOrder)
            ? Sort.by(entitySortField).descending()
            : Sort.by(entitySortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryEntity> pageEntities = categoryRepository.getCategoriesBy(enterpriseId, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    @Override
    public long countByEnterpriseId(String enterpriseId) {
        return categoryRepository.countByEnterpriseId(enterpriseId);
    }

    @Override
    public Page<Category> getActiveCategoriesBy(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        String entitySortField = mapCategorySortField(sortField);
        Sort sort = "desc".equalsIgnoreCase(sortOrder)
            ? Sort.by(entitySortField).descending()
            : Sort.by(entitySortField).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<CategoryEntity> pageEntities = categoryRepository.getActiveCategoriesBy(enterpriseId, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    @Override
    public long countActiveByEnterpriseId(String enterpriseId) {
        return categoryRepository.countActiveByEnterpriseId(enterpriseId);
    }

    /**
     * @brief Convierte entidad JPA a objeto de dominio Category
     * @param categoryEntity entidad a convertir
     * @return objeto Category del dominio
     */
    private Category convertToCategory(CategoryEntity categoryEntity) {
        return categoryPersistenceMapper.toCategory(categoryEntity);
    }

    /**
     * @brief Mapea campo de ordenamiento del dominio a campo de entidad
     * @param sortField campo de ordenamiento del dominio
     * @return campo de ordenamiento de la entidad
     */
    private String mapCategorySortField(String sortField) {
        return switch (sortField.toLowerCase()) {
            case "name" -> "name";
            case "description" -> "description";
            case "state" -> "state";
            default -> "name"; // Default ordering by name
        };
    }
}
