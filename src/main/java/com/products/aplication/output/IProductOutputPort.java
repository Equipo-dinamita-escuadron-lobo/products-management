package com.products.aplication.output;

import com.products.domain.models.Product;

import java.util.List;

public interface IProductOutputPort {
    Product createProduct (Product product);
    List<Product> getAllProduct ();
    Product getById(String id);

}
