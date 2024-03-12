package com.softwareContable.product.application;

import com.softwareContable.product.domain.models.Product;
import com.softwareContable.product.domain.repositories.IProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final IProductRepository productRepository;

    @Autowired
    public ProductService(IProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Optional<Product> getProductById(String id) {
        return productRepository.findById(id);
    }

    public Product createProduct(Product product) {
        //TODO Realizar validaciones antes de guardar el producto
        return productRepository.save(product);
    }

    public Product updateProduct(String id, Product product) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            //TODO realizar validaciones antes de actualizar el producto
            return productRepository.save(product);
        } else {
            //TODO Manejo de error si el producto no existe
            throw new IllegalArgumentException("Producto con id " + id + " no encontrado");
        }
    }

    public void deleteProduct(String id) {
        //TODO realizar validaciones antes de eliminar el producto
        productRepository.deleteById(id);
    }
}
