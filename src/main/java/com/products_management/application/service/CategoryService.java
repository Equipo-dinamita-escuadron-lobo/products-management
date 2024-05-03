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

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryServicePort {

  private final ICategoryPersistencePort categoryPersistencePort;
  private final ProductService productServicePort;

    @Override
    public Category findById(Long id) {
        return categoryPersistencePort.findById(id).orElseThrow(CategoryNotFoundException::new);
    }

    @Override
    public List<Category> findAll(String enterpriseId) {
        List<Category> allCategorys = categoryPersistencePort.findAll();
        return allCategorys.stream()
                .filter(category -> category.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Category> findActivated(String enterpriseId) {
        List<Category> allCategorys = categoryPersistencePort.findAll();
        return allCategorys.stream()
                .filter(category -> category.getState() .equals("true"))
                .filter(category -> category.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
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
      if (categoryPersistencePort.findById(id).isEmpty()) {
        throw new CategoryNotFoundException();
      }
        List<Product> products = productServicePort.findAllByCategory(id);
        if(!products.isEmpty()){
            throw new CategoryAssociatedException();
        }
        categoryPersistencePort.deleteById(id);
    }

    @Override
    public void deleteAll() {
        categoryPersistencePort.deleteAll();
    }
}
