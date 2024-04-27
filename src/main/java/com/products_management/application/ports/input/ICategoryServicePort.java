package com.products_management.application.ports.input;

import com.products_management.domain.model.Category;
import java.util.List;

public interface ICategoryServicePort {

    Category findById(Long id);
    List<Category> findAll();
    Category create(Category category);
    Category update(Long id, Category category);
    void deleteById(Long id);

}
