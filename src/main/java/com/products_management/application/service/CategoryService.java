package com.products_management.application.service;

import com.products_management.application.ports.input.ICategoryServicePort;
import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.domain.exception.CategoryNotFoundException;
import com.products_management.domain.model.Category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryServicePort {

    private final ICategoryPersistencePort categoryPersistencePort;

    @Override
    public Category findById(Long id) {
        return categoryPersistencePort.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public List<Category> findAll() {
        return categoryPersistencePort.findAll();
    }

    @Override
    public Category create(Category category) {
        return categoryPersistencePort.create(category);
    }

    @Override
    public Category update(Long id, Category category) {
        return categoryPersistencePort.findById(id)
                .map(create ->{
                    create.setName(category.getName());
                    create.setDescription(category.getDescription());
                    create.setEnterpriseId(category.getEnterpriseId());
                    create.setInventoryId(category.getInventoryId());
                    create.setCostId(category.getCostId());
                    create.setSaleId(category.getSaleId());
                    create.setReturnId(category.getReturnId());
                    return categoryPersistencePort.create(create);
                })
                .orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public void changeState(Long id) {
    Category category = categoryPersistencePort.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException());
        if ("true".equals(category.getState())) {
            category.setState("false"); 
        } else {
            category.setState("true"); 
        }
        categoryPersistencePort.create(category);
    }

    @Override
    public void deleteById(Long id) {
        if(categoryPersistencePort.findById(id).isEmpty()){
            throw new CategoryNotFoundException();
        }
        categoryPersistencePort.deleteById(id);
    }

    @Override
    public void deleteAll() {
        categoryPersistencePort.deleteAll();
    }
}
