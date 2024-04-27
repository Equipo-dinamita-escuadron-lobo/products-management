package com.products_management.application.ports.output;

import com.products_management.domain.model.Category;
import java.util.List;
import java.util.Optional;

public interface ICategoryPersistencePort {

    Optional<Category> findById(Long id);
    List<Category> findAll();
    Category create(Category unitOfMeasure);
    void deleteById(Long id);

}
