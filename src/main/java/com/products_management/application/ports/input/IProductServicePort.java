package com.products_management.application.ports.input;

import java.util.List;
import com.products_management.domain.model.Product;

public interface IProductServicePort {

  Product findById(Long id);
  List<Product> findAll(String enterpriseId);
  List<Product> findActivated(String enterpriseId);
  Product create(Product product);
  Product update(Long id, Product product);
  void deleteById(Long id);
  void changeState(Long id);
  void deleteAll();
  
}
