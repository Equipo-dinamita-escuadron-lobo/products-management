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

import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para la entidad Categoría.
 * Implementa la interfaz ICategoryPersistencePort para proporcionar métodos de persistencia.
 */
@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final ICategoryPersistenceMapper categoryPersistenceMapper;

    /**
     * Busca una categoría por su ID y empresa.
     *
     * @param id el ID de la categoría
     * @param enterpriseId el ID de la empresa
     * @return un Optional que contiene la categoría si se encuentra, de lo contrario vacío
     */
    @Override
    public Optional<Category> findByIdAndEnterpriseId(Long id, String enterpriseId) {
        return categoryRepository.findByIdAndEnterpriseId(id, enterpriseId)
                .map(categoryPersistenceMapper::toCategory);
    }

    /**
     * Crea una nueva categoría.
     *
     * @param category la categoría a crear
     * @return la categoría creada
     */
    @Override
    public Category create(Category category) {
        return categoryPersistenceMapper.toCategory(categoryRepository.save(categoryPersistenceMapper.toCategoryEntity(category)));
    }

    /**
     * Elimina una categoría por su ID.
     *
     * @param id el ID de la categoría a eliminar
     */
    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(Long.valueOf(id));
    }


    /**
     * Busca categorías activas por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @param state el estado de la categoría
     * @return una lista de categorías activas de la empresa
     */
    @Override
    public List<Category> findByEnterpriseIdAndState(String enterpriseId, boolean state) {
        return categoryPersistenceMapper.toCategoryList(
                categoryRepository.findByEnterpriseIdAndState(enterpriseId, state));
    }

    /**
     * Verifica si existe una categoría con el nombre especificado para una empresa.
     *
     * @param name el nombre de la categoría
     * @param enterpriseId el ID de la empresa
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsByNameAndEnterpriseId(String name, String enterpriseId) {
        return categoryRepository.existsByNameAndEnterpriseId(name, enterpriseId);
    }

    /**
     * Verifica si existe una categoría con el nombre especificado para una empresa, excluyendo un ID específico.
     *
     * @param name el nombre de la categoría
     * @param enterpriseId el ID de la empresa
     * @param id el ID a excluir de la búsqueda
     * @return true si existe, false en caso contrario
     */
    @Override
    public boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id) {
        return categoryRepository.existsByNameAndEnterpriseIdAndIdNot(name, enterpriseId, id);
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
        Page<CategoryEntity> pageEntities = categoryRepository.getCategoriesBy(enterpriseId, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    /**
     * Obtiene todas las categorías de una empresa filtradas por estado.
     * Optimizado para exportación: el filtro se aplica en BD, no en memoria.
     *
     * @param enterpriseId El identificador de la entidad
     * @param state Estado de las categorías (true=activas, false=inactivas)
     * @param pageable El objeto Pageable que contiene la información de paginación
     * @return Una página de objetos Category filtrados por estado
     */
    @Override
    public Page<Category> getAllCategoriesByState(String enterpriseId, Boolean state, Pageable pageable) {
        Page<CategoryEntity> pageEntities = categoryRepository.getCategoriesByEnterpriseIdAndState(enterpriseId, state, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    /**
     * Busca categorías por empresa y término de búsqueda.
     * Busca en: nombres, descripción.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @param pageable Paginación con ordenamiento
     * @return Página de categorías que coinciden con la búsqueda
     */
    @Override
    public Page<Category> findByEnterpriseIdAndSearch(String enterpriseId, String search, Pageable pageable) {
        Page<CategoryEntity> pageEntities = categoryRepository.findByEnterpriseIdAndSearch(enterpriseId, search, pageable);
        Page<Category> pageCategories = pageEntities.map(this::convertToCategory);

        return pageCategories;
    }

    /**
     * Cuenta categorías por empresa y término de búsqueda.
     *
     * @param enterpriseId ID de la empresa
     * @param search Término de búsqueda
     * @return Cantidad de categorías que coinciden
     */
    @Override
    public long countByEnterpriseIdAndSearch(String enterpriseId, String search) {
        return categoryRepository.countByEnterpriseIdAndSearch(enterpriseId, search);
    }

    /**
     * Obtiene todas las categorías con ordenamiento.
     * @param enterpriseId El id de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías ordenadas
     */
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

    /**
     * Cuenta el total de categorías por empresa.
     * @param enterpriseId El id de la empresa
     * @return El número total de categorías
     */
    @Override
    public long countByEnterpriseId(String enterpriseId) {
        return categoryRepository.countByEnterpriseId(enterpriseId);
    }

    /**
     * Obtiene todas las categorías activas de una empresa con ordenamiento.
     *
     * @param enterpriseId ID de la empresa
     * @param page Número de página
     * @param size Tamaño de página
     * @param sortField Campo de ordenamiento
     * @param sortOrder Orden (asc/desc)
     * @return Página de categorías activas
     */
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

    /**
     * Cuenta el total de categorías activas por empresa.
     *
     * @param enterpriseId ID de la empresa
     * @return Cantidad total de categorías activas
     */
    @Override
    public long countActiveByEnterpriseId(String enterpriseId) {
        return categoryRepository.countActiveByEnterpriseId(enterpriseId);
    }

    /**
     * Convierte un objeto CategoryEntity a un objeto Category.
     * @param categoryEntity El objeto CategoryEntity que se va a convertir.
     * @return El objeto Category resultante de la conversión.
     */
    private Category convertToCategory(CategoryEntity categoryEntity) {
        return categoryPersistenceMapper.toCategory(categoryEntity);
    }

    /**
     * Mapea el campo de ordenamiento del dominio al campo de la entidad.
     * @param sortField Campo de ordenamiento del dominio
     * @return Campo de ordenamiento de la entidad
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
