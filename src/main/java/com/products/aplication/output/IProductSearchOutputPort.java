package com.products.aplication.output;

import com.products.domain.models.Product;

public interface IProductSearchOutputPort {
    Product getById(String id);
}
