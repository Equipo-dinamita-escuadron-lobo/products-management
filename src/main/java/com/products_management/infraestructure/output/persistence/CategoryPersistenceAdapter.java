package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.mapper.impl.CategoryPersistenceMapperImpl;
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
    private final CategoryPersistenceMapperImpl categoryPersistenceMapper;

    /**
     * Busca una categoría por su ID.
     *
     * @param id el ID de la categoría
     * @return un Optional que contiene la categoría si se encuentra, de lo contrario vacío
     */
    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(Long.valueOf(id))
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
     * Elimina todas las categorías.
     */
    @Override
    public void deleteAll() {
        categoryRepository.deleteAll();
    }
}
