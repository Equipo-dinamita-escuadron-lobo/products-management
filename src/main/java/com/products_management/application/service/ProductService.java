package com.products_management.application.service;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.exception.ProductNotFoundException;
import com.products_management.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;

    @Override
    public Product findByCodeProduct(String code) {
        return productPersistencePort.findByCodeProduct(code).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<Product> findAllProducts() {
        return productPersistencePort.findAllProducts();
    }

    @Override
    public Product createProduct(Product product) {
        return productPersistencePort.createProduct(product);
    }

    @Override
    public Product updateProduct(String code, Product product) {
        return productPersistencePort.findByCodeProduct(code)
                .map(createProduct ->{
                    createProduct.setItemType(product.getItemType());
                    createProduct.setDescription(product.getDescription());
                    createProduct.setMinQuantity(product.getMinQuantity());
                    createProduct.setMaxQuantity(product.getMaxQuantity());
                    createProduct.setTaxPercentage(product.getTaxPercentage());
                    createProduct.setUnitOfMeasure(product.getUnitOfMeasure());
                    createProduct.setSupplier(product.getSupplier());
                    createProduct.setCategory(product.getCategory());
                    createProduct.setPrice(product.getPrice());
                    return productPersistencePort.createProduct(createProduct);
                })
                .orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public void deleteByCodeProduct(String code) {
        if(productPersistencePort.findByCodeProduct(code).isEmpty()){
            throw new ProductNotFoundException();
        }
        productPersistencePort.deleteByCodeProduct(code);
    }
}
