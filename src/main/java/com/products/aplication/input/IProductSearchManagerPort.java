package com.products.aplication.input;

import com.products.domain.models.Product;

public interface IProductSearchManagerPort {

    Product getByIdProduct(String id);

}
