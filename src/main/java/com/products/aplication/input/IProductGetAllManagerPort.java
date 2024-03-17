package com.products.aplication.input;

import com.products.domain.models.Product;

import java.util.List;

public interface IProductGetAllManagerPort  {
    List<Product> getAllProducts();
}
