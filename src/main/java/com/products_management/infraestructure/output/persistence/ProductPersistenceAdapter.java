package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductPersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Optional;

/**
 * Adaptador de persistencia para la entidad Producto.
 * Implementa la interfaz IProductPersistencePort para proporcionar métodos de persistencia.
 */
@Component
@RequiredArgsConstructor
public class ProductPersistenceAdapter implements IProductPersistencePort {

    private final IProductRepository productRepository;
    private final IProductPersistenceMapper productPersistenceMapper;

    /**
     * Busca un producto por su ID.
     *
     * @param id el ID del producto
     * @return un Optional que contiene el producto si se encuentra, de lo contrario vacío
     */
    @Override
    public Optional<Product> findById(Long id) {
        return productRepository.findById(Long.valueOf(id))
                .map(productPersistenceMapper::toProduct);
    }

    /**
     * Obtiene una lista de todos los productos.
     *
     * @return una lista de productos
     */
    @Override
    public List<Product> findAll() {
        return productPersistenceMapper.toProductList(productRepository.findAll());
    }

    /**
     * Crea un nuevo producto.
     *
     * @param product el producto a crear
     * @return el producto creado
     */
    @Override
    public Product create(Product product) {
        return productPersistenceMapper.toProduct(productRepository.save(productPersistenceMapper.toProductEntity(product)));
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id el ID del producto a eliminar
     */
    @Override
    public void deleteById(Long id) {
        productRepository.deleteById(Long.valueOf(id));
    }

    /**
     * Elimina todos los productos.
     */
    @Override
    public void deleteAll() {
        productRepository.deleteAll();
    }

    /**
     * Busca productos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return una lista de productos de la empresa
     */
    @Override
    public List<Product> findByEnterpriseId(String enterpriseId) {
        return productPersistenceMapper.toProductList(
                productRepository.findByEnterpriseId(enterpriseId));
    }

    /**
     * Busca productos activos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @param state el estado del producto
     * @return una lista de productos activos de la empresa
     */
    @Override
    public List<Product> findByEnterpriseIdAndState(String enterpriseId, boolean state) {
        return productPersistenceMapper.toProductList(
                productRepository.findByEnterpriseIdAndState(enterpriseId, state));
    }

    /**
     * Busca productos por ID de categoría.
     *
     * @param categoryId el ID de la categoría
     * @return una lista de productos de la categoría
     */
    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return productPersistenceMapper.toProductList(
                productRepository.findByCategoryId(categoryId));
    }

    /**
     * Busca productos por ID de unidad de medida.
     *
     * @param unitOfMeasureId el ID de la unidad de medida
     * @return una lista de productos con esa unidad de medida
     */
    @Override
    public List<Product> findByUnitOfMeasureId(Long unitOfMeasureId) {
        return productPersistenceMapper.toProductList(
                productRepository.findByUnitOfMeasureId(unitOfMeasureId));
    }

    /**
     * Busca productos por ID de tipo de producto.
     *
     * @param productTypeId el ID del tipo de producto
     * @return una lista de productos de ese tipo de producto
     */
    @Override
    public List<Product> findByProductTypeId(Long productTypeId) {
        return productPersistenceMapper.toProductList(
                productRepository.findByProductTypeId(productTypeId));
    }
}
