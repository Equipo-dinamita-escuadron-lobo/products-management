package com.products_management.application.ports.input;

import java.util.List;
import com.products_management.domain.model.Product;

public interface IProductServicePort {

  Product findByCodeProduct(String code);

  List<Product> findAllProducts();

  Product createProduct(Product product);

  Product updateProduct(String code, Product product);

  void deleteByCodeProduct(String code);  
  
}
