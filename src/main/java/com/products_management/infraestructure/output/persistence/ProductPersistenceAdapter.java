package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductPersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.IProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
     * Busca un producto por su ID y empresa.
     *
     * @param id el ID del producto
     * @param enterpriseId el ID de la empresa
     * @return un Optional que contiene el producto si se encuentra, de lo contrario vacío
     */
    @Override
    public Optional<Product> findByIdAndEnterpriseId(Long id, String enterpriseId) {
        return productRepository.findByIdAndEnterpriseId(id, enterpriseId)
                .map(productPersistenceMapper::toProduct);
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
     * Busca productos activos por ID de empresa con paginación.
     *
     * @param enterpriseId el ID de la empresa
     * @param pageNumber el número de página
     * @param pageSize el tamaño de página
     * @return una página de productos activos
     */
    @Override
    public Page<Product> findActivatedWithPagination(String enterpriseId, int pageNumber, int pageSize) {
        Sort sort = Sort.by("name").ascending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findByEnterpriseIdAndState(enterpriseId, true, pageRequest)
                .map(productPersistenceMapper::toProduct);
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

    @Override
    public boolean existsByNameAndEnterpriseId(String name, String enterpriseId) {
        return productRepository.existsByNameAndEnterpriseId(name, enterpriseId);
    }

    @Override
    public boolean existsByReferenceAndEnterpriseId(String reference, String enterpriseId) {
        return productRepository.existsByReferenceAndEnterpriseId(reference, enterpriseId);
    }

    @Override
    public boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id) {
        return productRepository.existsByNameAndEnterpriseIdAndIdNot(name, enterpriseId, id);
    }

    @Override
    public boolean existsByReferenceAndEnterpriseIdAndIdNot(String reference, String enterpriseId, Long id) {
        return productRepository.existsByReferenceAndEnterpriseIdAndIdNot(reference, enterpriseId, id);
    }

    /**
     * Busca productos por ID de empresa con filtros de búsqueda y paginación.
     *
     * @param enterpriseId el ID de la empresa
     * @param search el término de búsqueda
     * @param pageNumber el número de página
     * @param pageSize el tamaño de página
     * @param sortField el campo de ordenamiento
     * @param sortOrder el orden (asc/desc)
     * @return una página de productos
     */
    @Override
    public Page<Product> findByEnterpriseIdWithFilters(String enterpriseId, String search, int pageNumber, int pageSize, String sortField, String sortOrder) {
        Sort sort = sortOrder.equalsIgnoreCase("desc") ? Sort.by(sortField).descending() : Sort.by(sortField).ascending();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize, sort);
        return productRepository.findByEnterpriseIdWithFilters(enterpriseId, search, pageRequest)
                .map(productPersistenceMapper::toProduct);
    }

    /**
     * Cuenta productos por ID de empresa con filtros de búsqueda.
     *
     * @param enterpriseId el ID de la empresa
     * @param search el término de búsqueda
     * @return el número de productos que coinciden
     */
    @Override
    public long countByEnterpriseIdWithFilters(String enterpriseId, String search) {
        return productRepository.countByEnterpriseIdWithFilters(enterpriseId, search);
    }

    /**
     * Cuenta todos los productos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return el número total de productos
     */
    @Override
    public long countByEnterpriseId(String enterpriseId) {
        return productRepository.countByEnterpriseId(enterpriseId);
    }
    
    /**
     * Cuenta productos activos por ID de empresa.
     *
     * @param enterpriseId el ID de la empresa
     * @return el número de productos activos
     */
    @Override
    public long countActivatedByEnterpriseId(String enterpriseId) {
        return productRepository.countByEnterpriseIdAndState(enterpriseId, true);
    }
}
