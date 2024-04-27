package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.domain.model.Category;
import com.products_management.infraestructure.output.persistence.mapper.impl.CategoryPersistenceMapperImpl;
import com.products_management.infraestructure.output.persistence.repository.ICategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CategoryPersistenceAdapter implements ICategoryPersistencePort {

    private final ICategoryRepository categoryRepository;
    private final CategoryPersistenceMapperImpl categoryPersistenceMapper;

    @Override
    public Optional<Category> findById(Long id) {
        return categoryRepository.findById(Long.valueOf(id))
                .map(categoryPersistenceMapper::toCategory);
    }

    @Override
    public List<Category> findAll() {
        return categoryPersistenceMapper.toCategoryList(categoryRepository.findAll());
    }

    @Override
    public Category create(Category category) {
        return categoryPersistenceMapper.toCategory(categoryRepository.save(categoryPersistenceMapper.toCategoryEntity(category)));
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(Long.valueOf(id));
    }
}
