package com.products_management.application.service;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.exception.ProductNotFoundException;
import com.products_management.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;

    @Override
    public Product findById(Long id) {
        return productPersistencePort.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    @Override
    public List<Product> findAll() {
        return productPersistencePort.findAll();
    }

    @Override
    public List<Product> findActivated(String state) {
        System.out.println("****************** Estado del producto: "+state);
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> product.getState() .equals(state))
                .collect(Collectors.toList());
    }

    @Override
    public Product create(Product product) {
        return productPersistencePort.create(product);
    }

    @Override
    public Product update(Long id, Product product) {
        return productPersistencePort.findById(id)
                .map(create ->{
                    create.setItemType(product.getItemType());
                    create.setDescription(product.getDescription());
                    create.setMinQuantity(product.getMinQuantity());
                    create.setMaxQuantity(product.getMaxQuantity());
                    create.setTaxPercentage(product.getTaxPercentage());
                    create.setUnitOfMeasureId(product.getUnitOfMeasureId());
                    create.setSupplierId(product.getSupplierId());
                    create.setCategoryId(product.getCategoryId());
                    create.setEnterpriseId(product.getEnterpriseId());
                    create.setPrice(product.getPrice());
                    return productPersistencePort.create(create);
                })
                .orElseThrow(ProductNotFoundException::new);
    }

    public void deleteById(Long id) {
        Product product = productPersistencePort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException());
    
        product.setState("false"); 
        productPersistencePort.create(product);
    }

}
