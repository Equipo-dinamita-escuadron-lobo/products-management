package com.products.domain.services;

import org.springframework.stereotype.Service;

import com.products.aplication.input.IProductSearchManagerPort;
import com.products.aplication.input.IProductCreateManagerPort;
import com.products.aplication.output.IProductCreateOutputPort;
import com.products.aplication.output.IProductSearchOutputPort;
import com.products.domain.models.Product;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ProductServices implements IProductSearchManagerPort, IProductCreateManagerPort{

    private final IProductSearchOutputPort outputPortSearch;

    @Override
    public Product getByIdProduct(String id) {
        return outputPortSearch.getById(id);
    }

    private final IProductCreateOutputPort outputPortCreate;

    @Override
    public Product createProduct(Product product){
        return outputPortCreate.createProduct(product);
    }
    
    
}
