package com.products.aplication.input;

import com.products.domain.models.Product;

public interface IProductCreateManagerPort {
    Product createProduct(Product product);
}
