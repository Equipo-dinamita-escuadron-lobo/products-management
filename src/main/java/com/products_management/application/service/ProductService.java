package com.products_management.application.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.products_management.application.dto.ProductSyncDto;
import com.products_management.application.ports.input.IProductEventPort;
import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.output.ICategoryPersistencePort;
import com.products_management.application.ports.output.IProductPersistencePort;
import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.application.ports.output.IUnitOfMeasurePersistencePort;
import com.products_management.domain.exception.category.CategoryNotFoundException;
import com.products_management.domain.exception.productType.ProductTypeNotFoundException;
import com.products_management.domain.exception.unitOfMeasure.UnitOfMeasureNotFoundException;
import com.products_management.domain.exception.product.ProductNotFoundException;
import com.products_management.domain.exception.product.ProductNameAlreadyExistsException;
import com.products_management.domain.exception.product.ProductReferenceAlreadyExistsException;
import com.products_management.domain.model.Product;
import com.products_management.domain.utils.StringNormalizer;

import lombok.RequiredArgsConstructor;

/**
 * Servicio que implementa la lógica de negocio para los productos.
 * Esta clase interactúa con los puertos de persistencia y realiza las
 * operaciones
 * necesarias para gestionar los productos.
 */
@Service
@RequiredArgsConstructor
public class ProductService implements IProductServicePort {

    private final IProductPersistencePort productPersistencePort;
    private final IProductEventPort productEventPort;
    private final IUnitOfMeasurePersistencePort unitOfMeasurePersistencePort;
    private final ICategoryPersistencePort categoryPersistencePort;
    private final IProductTypePersistencePort productTypePersistencePort;

    /**
     * Busca un producto por su ID y empresa.
     *
     * @param id el ID del producto a buscar.
     * @param enterpriseId el ID de la empresa.
     * @return el producto encontrado.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */

    @Override
    public Product findById(Long id, String enterpriseId) {
        return productPersistencePort.findByIdAndEnterpriseId(id, enterpriseId).orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Obtiene una lista de todos los productos asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los productos de la empresa.
     */

    @Override
    public List<Product> findAll(String enterpriseId) {
        return productPersistencePort.findByEnterpriseId(enterpriseId);
    }

    /**
     * Obtiene una lista de todos los productos activados asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los productos activados de la empresa.
     */

    @Override
    public List<Product> findActivated(String enterpriseId) {
        return productPersistencePort.findByEnterpriseIdAndState(enterpriseId, true);
    }

    /**
     * Crea un nuevo producto.
     *
     * @param product el producto a crear.
     * @return el producto creado.
     */

    @Override
    public Product create(Product product) {
        // Normalizar nombre y referencia de productos en mayúsculas (formato estándar para productos)
        product.setName(StringNormalizer.normalizeCode(product.getName()));
        if (product.getReference() != null && !product.getReference().trim().isEmpty()) {
            product.setReference(StringNormalizer.normalizeCode(product.getReference()));
        }
        
        // Validar existencia de entidades relacionadas
        validateRelatedEntitiesExistence(product);
        
        // Validar unicidad antes de crear
        validateProductUniqueness(product);
        
        // Primero guardar para obtener el ID
        Product createdProduct = productPersistencePort.create(product);

        // Ahora generar el código con el ID real
        createdProduct.generateCode();

        // Actualizar con el código generado
        createdProduct = productPersistencePort.create(createdProduct);

        ProductSyncDto productSyncDto = new ProductSyncDto(
                createdProduct.getId(),
                createdProduct.getName(),
                createdProduct.getReference(),
                createdProduct.getEnterpriseId(),
                createdProduct.getPresentation(),
                createdProduct.getQuantity(),
                createdProduct.getCost(),
                createdProduct.isState());
        productEventPort.publishCreatedStockEvent(productSyncDto);
        return createdProduct;
    }

    /**
     * Actualiza un producto existente.
     *
     * @param id      el ID del producto a actualizar.
     * @param product los datos del producto actualizado.
     * @param enterpriseId el ID de la empresa.
     * @return el producto actualizado.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */

    @Override
    public Product update(Long id, Product product, String enterpriseId) {
        return productPersistencePort.findByIdAndEnterpriseId(id, enterpriseId)
                .map(existingProduct -> {
                    // Normalizar nombre y referencia de productos en mayúsculas (formato estándar para productos)
                    product.setName(StringNormalizer.normalizeCode(product.getName()));
                    if (product.getReference() != null && !product.getReference().trim().isEmpty()) {
                        product.setReference(StringNormalizer.normalizeCode(product.getReference()));
                    }
                    
                    // Validar existencia de entidades relacionadas
                    validateRelatedEntitiesExistence(product);
                    
                    // Validar unicidad antes de actualizar
                    validateProductUniquenessForUpdate(id, product);
                    
                    boolean shouldRegenerateCode = !existingProduct.getName().equals(product.getName()) ||
                            !existingProduct.getCategoryId().equals(product.getCategoryId()) ||
                            !existingProduct.getId().equals(product.getId());

                    existingProduct.setName(product.getName());
                    existingProduct.setDescription(product.getDescription());
                    existingProduct.setQuantity(product.getQuantity());
                    existingProduct.setTaxes(product.getTaxes());
                    existingProduct.setUnitOfMeasureId(product.getUnitOfMeasureId());
                    existingProduct.setCategoryId(product.getCategoryId());
                    existingProduct.setCost(product.getCost());
                    existingProduct.setProductTypeId(product.getProductTypeId());
                    existingProduct.setReference(product.getReference());

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
     * @param enterpriseId el ID de la empresa.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */

    @Override
    public void changeState(Long id, String enterpriseId) {
        Product product = productPersistencePort.findByIdAndEnterpriseId(id, enterpriseId)
                .orElseThrow(() -> new ProductNotFoundException());
        product.setState(!product.isState());
        productPersistencePort.create(product);
    }

    /**
     * Elimina un producto por su ID.
     *
     * @param id el ID del producto a eliminar.
     * @param enterpriseId el ID de la empresa.
     * @throws ProductNotFoundException si el producto no se encuentra.
     */

    @Override
    public void deleteById(Long id, String enterpriseId) {
        if (productPersistencePort.findByIdAndEnterpriseId(id, enterpriseId).isEmpty()) {
            throw new ProductNotFoundException();
        }
        productPersistencePort.deleteById(id);
    }

    /**
     * Obtiene una lista de todos los productos asociados a una categoría.
     *
     * @param categoryId el ID de la categoría.
     * @return una lista de todos los productos de la categoría.
     */

    @Override
    public List<Product> findAllByCategory(Long categoryId) {
        return productPersistencePort.findByCategoryId(categoryId);
    }

    /**
     * Obtiene una lista de todos los productos asociados a una unidad de medida.
     *
     * @param unitOfMeasureId el ID de la unidad de medida.
     * @return una lista de todos los productos de la unidad de medida.
     */

    @Override
    public List<Product> findAllByUnitOfMeasure(Long unitOfMeasureId) {
        return productPersistencePort.findByUnitOfMeasureId(unitOfMeasureId);
    }

    /**
     * Obtiene una lista de todos los productos asociados a un tipo de producto.
     *
     * @param productTypeId el ID del tipo de producto.
     * @return una lista de todos los productos del tipo de producto.
     */

    @Override
    public List<Product> findAllByProductType(Long productTypeId) {
        return productPersistencePort.findByProductTypeId(productTypeId);
    }
    
    /**
     * Valida que el nombre y la referencia de un producto sean únicos dentro de la empresa.
     *
     * @param product el producto a validar.
     * @throws ProductNameAlreadyExistsException si ya existe un producto con el mismo nombre.
     * @throws ProductReferenceAlreadyExistsException si ya existe un producto con la misma referencia.
     */
    private void validateProductUniqueness(Product product) {
        // El nombre ya está normalizado, se usa directamente para validación
        if (productPersistencePort.existsByNameAndEnterpriseId(
                product.getName(), product.getEnterpriseId())) {
            throw new ProductNameAlreadyExistsException(product.getName());
        }
        
        // Validar referencia solo si no es null o vacía
        if (product.getReference() != null && !product.getReference().trim().isEmpty()) {
            if (productPersistencePort.existsByReferenceAndEnterpriseId(
                    product.getReference(), product.getEnterpriseId())) {
                throw new ProductReferenceAlreadyExistsException(product.getReference());
            }
        }
    }
    
    /**
     * Valida que el nombre y la referencia de un producto sean únicos dentro de la empresa
     * durante una actualización, excluyendo el producto que se está actualizando.
     *
     * @param id el ID del producto que se está actualizando.
     * @param product el producto a validar.
     * @throws ProductNameAlreadyExistsException si ya existe otro producto con el mismo nombre.
     * @throws ProductReferenceAlreadyExistsException si ya existe otro producto con la misma referencia.
     */
    private void validateProductUniquenessForUpdate(Long id, Product product) {
        // El nombre ya está normalizado, se usa directamente para validación
        if (productPersistencePort.existsByNameAndEnterpriseIdAndIdNot(
                product.getName(), product.getEnterpriseId(), id)) {
            throw new ProductNameAlreadyExistsException(product.getName());
        }
        
        // Validar referencia solo si no es null o vacía
        if (product.getReference() != null && !product.getReference().trim().isEmpty()) {
            if (productPersistencePort.existsByReferenceAndEnterpriseIdAndIdNot(
                    product.getReference(), product.getEnterpriseId(), id)) {
                throw new ProductReferenceAlreadyExistsException(product.getReference());
            }
        }
    }
    
    /**
     * Valida que las entidades relacionadas (unidad de medida, categoría, tipo de producto) existan.
     *
     * @param product el producto cuyas entidades relacionadas se van a validar.
     * @throws UnitOfMeasureNotFoundException si la unidad de medida no existe.
     * @throws CategoryNotFoundException si la categoría no existe.
     * @throws ProductTypeNotFoundException si el tipo de producto no existe.
     */
    private void validateRelatedEntitiesExistence(Product product) {
        // Validar unidad de medida
        if (product.getUnitOfMeasureId() != null) {
            if (unitOfMeasurePersistencePort.findByIdAndEnterpriseId(
                    product.getUnitOfMeasureId(), product.getEnterpriseId()).isEmpty()) {
                throw new UnitOfMeasureNotFoundException(product.getUnitOfMeasureId());
            }
        }
        
        // Validar categoría
        if (product.getCategoryId() != null) {
            if (categoryPersistencePort.findByIdAndEnterpriseId(
                    product.getCategoryId(), product.getEnterpriseId()).isEmpty()) {
                throw new CategoryNotFoundException(product.getCategoryId());
            }
        }
        
        // Validar tipo de producto
        if (product.getProductTypeId() != null) {
            if (productTypePersistencePort.findByIdAndEnterpriseId(
                    product.getProductTypeId(), product.getEnterpriseId()).isEmpty()) {
                throw new ProductTypeNotFoundException(product.getProductTypeId());
            }
        }
    }
}
