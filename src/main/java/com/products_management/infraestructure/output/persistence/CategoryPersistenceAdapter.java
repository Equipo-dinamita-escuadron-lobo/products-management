package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.ICategoryPersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
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
     * Obtiene una lista de todas las categorías.
     *
     * @return una lista de categorías
     */
    @Override
    public List<Category> findAll() {
        return categoryPersistenceMapper.toCategoryList(categoryRepository.findAll());
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
     * Busca categorías por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return una lista de categorías de la empresa
     */
    @Override
    public List<Category> findByEnterpriseId(String enterpriseId) {
        return categoryPersistenceMapper.toCategoryList(
                categoryRepository.findByEnterpriseId(enterpriseId));
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
}
