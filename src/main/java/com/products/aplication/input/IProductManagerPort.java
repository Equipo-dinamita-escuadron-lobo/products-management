package com.products.aplication.input;

import com.products.domain.models.Product;

import java.util.List;

public interface IProductManagerPort {
    Product createProduct(Product product);
    List<Product> getAllProducts();
    Product getByIdProduct(String id);

}
