package com.products.domain.services;

import com.products.aplication.input.IProductManagerPort;
import com.products.aplication.output.IProductOutputPort;
import com.products.domain.models.Product;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ProductServices implements IProductManagerPort {

    private final IProductOutputPort productOutputPort;

    @Override
    public Product getByIdProduct(String id) {
        return productOutputPort.getById(id);
    }

    @Override
    public Product createProduct(Product product) {
        return productOutputPort.createProduct(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return productOutputPort.getAllProduct();

    }
}
