package com.products_management.application.service;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.exception.ProductNotFoundException;
import com.products_management.domain.model.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Servicio que implementa la lógica de negocio para los productos.
 * Esta clase interactúa con los puertos de persistencia y realiza las operaciones
 * necesarias para gestionar los productos.
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;

    /**
     * Busca un producto por su ID.
     *
     * @param id el ID del producto a buscar.
     * @return el producto encontrado.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Override
    public Product findById(Long id) {
        return productPersistencePort.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Obtiene una lista de todos los productos asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los productos de la empresa.
     */
    @Override
    public List<Product> findAll(String enterpriseId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> product.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los productos activados asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los productos activados de la empresa.
     */
    @Override
    public List<Product> findActivated(String enterpriseId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> "true".equals(product.getState()))
                .filter(product -> product.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }

    /**
     * Crea un nuevo producto.
     *
     * @param product el producto a crear.
     * @return el producto creado.
     */
    @Override
    public Product create(Product product) {
        product.generateCode();
        return productPersistencePort.create(product);
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id el ID del producto a actualizar.
     * @param product los datos del producto actualizado.
     * @return el producto actualizado.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Override
    public Product update(Long id, Product product) {
        return productPersistencePort.findById(id)
            .map(existingProduct -> {
                boolean shouldRegenerateCode = !existingProduct.getItemType().equals(product.getItemType()) || 
                                               !existingProduct.getCategoryId().equals(product.getCategoryId()) || 
                                               !existingProduct.getId().equals(product.getId());

                existingProduct.setItemType(product.getItemType());
                existingProduct.setDescription(product.getDescription());
                existingProduct.setMinQuantity(product.getMinQuantity());
                existingProduct.setMaxQuantity(product.getMaxQuantity());
                existingProduct.setTaxPercentage(product.getTaxPercentage());
                existingProduct.setUnitOfMeasureId(product.getUnitOfMeasureId());
                existingProduct.setCategoryId(product.getCategoryId());
                existingProduct.setPrice(product.getPrice());

                if (shouldRegenerateCode) {
                    existingProduct.generateCode();
                }

                return productPersistencePort.create(existingProduct);
            })
            .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Cambia el estado de un producto (activado/desactivado).
     *
     * @param id el ID del producto cuyo estado se va a cambiar.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Override
    public void changeState(Long id) {
        Product product = productPersistencePort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException());
        product.setState("true".equals(product.getState()) ? "false" : "true");
        productPersistencePort.create(product);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id el ID del producto a eliminar.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */
    @Override
    public void deleteById(Long id) {
        if (productPersistencePort.findById(id).isEmpty()) {
            throw new ProductNotFoundException();
        }
        productPersistencePort.deleteById(id);
    }

    /**
     * Elimina todos los productos.
     */
    @Override
    public void deleteAll() {
        productPersistencePort.deleteAll();
    }

    /**
     * Obtiene una lista de todos los productos asociados a una categoría.
     *
     * @param categoryId el ID de la categoría.
     * @return una lista de todos los productos de la categoría.
     */
    @Override
    public List<Product> findAllByCategory(Long categoryId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> product.getCategoryId().equals(categoryId))
                .collect(Collectors.toList());
    }

    /**
     * Obtiene una lista de todos los productos asociados a una unidad de medida.
     *
     * @param unitOfMeasureId el ID de la unidad de medida.
     * @return una lista de todos los productos de la unidad de medida.
     */
    @Override
    public List<Product> findAllByUnitOfMeasure(Long unitOfMeasureId) {
        List<Product> allProducts = productPersistencePort.findAll();
        return allProducts.stream()
                .filter(product -> product.getUnitOfMeasureId().equals(unitOfMeasureId))
                .collect(Collectors.toList());
    }
}
