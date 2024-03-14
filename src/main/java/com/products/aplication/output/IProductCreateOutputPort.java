package com.products.aplication.output;

import com.products.domain.models.Product;

public interface IProductCreateOutputPort {
    Product createProduct (Product product);
}
