package com.products_management.application.service;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.input.IProductTypeServicePort;
import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.domain.exception.productType.ProductTypeAssociatedException;
import com.products_management.domain.exception.productType.ProductTypeNotFoundException;
import com.products_management.domain.exception.productType.ProductTypeNameAlreadyExistsException;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.ProductType;
import com.products_management.domain.utils.StringNormalizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeService implements IProductTypeServicePort {

    private final IProductTypePersistencePort productTypeOutputPort;
    private final IProductServicePort productServicePort;

    public ProductTypeService(IProductTypePersistencePort productTypeOutputPort, IProductServicePort productServicePort) {
        this.productTypeOutputPort = productTypeOutputPort;
        this.productServicePort = productServicePort;
    }

    @Override
    public ProductType createProductType(ProductType productType) {
        // Normalizar el nombre de manera consistente (para validación y almacenamiento)
        productType.setName(StringNormalizer.normalize(productType.getName()));
        validateProductTypeUniqueness(productType);
        return productTypeOutputPort.save(productType);
    }

    @Override
    public List<ProductType> getProductTypesByEnterpriseId(String enterpriseId) {
        List<ProductType> productTypes = productTypeOutputPort.findByEnterpriseId(enterpriseId);
        return productTypes;
    }

    /**
     * Obtiene una lista de todos los tipos de producto activados asociados a una empresa.
     *
     * @param enterpriseId el ID de la empresa.
     * @return una lista de todos los tipos de producto activados de la empresa.
     */
    @Override
    public List<ProductType> findActivated(String enterpriseId) {
        return productTypeOutputPort.findByEnterpriseIdAndState(enterpriseId, true);
    }

    @Override
    public ProductType updateProductType(Long id, String enterpriseId, ProductType productType) {
        // Verificar que el tipo de producto existe y pertenece a la empresa antes de actualizar
        Optional<ProductType> existingProductType = productTypeOutputPort.findByIdAndEnterpriseId(id, enterpriseId);
        if (existingProductType.isEmpty()) {
            throw new ProductTypeNotFoundException();
        }
        
        // Normalizar el nombre de manera consistente (para validación y almacenamiento)
        productType.setName(StringNormalizer.normalize(productType.getName()));
        validateProductTypeUniquenessForUpdate(id, productType);
        
        return productTypeOutputPort.update(id, productType);
    }

    @Override
    public void deleteProductType(Long id, String enterpriseId) {
        // Verificar que el tipo de producto existe y pertenece a la empresa antes de eliminar
        Optional<ProductType> existingProductType = productTypeOutputPort.findByIdAndEnterpriseId(id, enterpriseId);
        if (existingProductType.isEmpty()) {
            throw new ProductTypeNotFoundException();
        }
        // Verificar que el tipo de producto no esté asociado a productos
        List<Product> products = productServicePort.findAllByProductType(id);
        if (!products.isEmpty()) {
            throw new ProductTypeAssociatedException();
        }
        productTypeOutputPort.delete(id);
    }

    @Override
    public Optional<ProductType> findById(Long id) {
        return productTypeOutputPort.findById(id);
    }
    
    /**
     * Busca un tipo de producto por ID y empresa, lanza excepción si no se encuentra.
     * 
     * @param id el ID del tipo de producto a buscar
     * @param enterpriseId el ID de la empresa
     * @return el tipo de producto encontrado
     * @throws ProductTypeNotFoundException si no se encuentra el tipo de producto
     */
    public ProductType getProductTypeByIdAndEnterpriseId(Long id, String enterpriseId) {
        return productTypeOutputPort.findByIdAndEnterpriseId(id, enterpriseId)
                .orElseThrow(() -> new ProductTypeNotFoundException());
    }
    
    /**
     * Cambia el estado de un tipo de producto (activado/desactivado).
     *
     * @param id el ID del tipo de producto cuyo estado se va a cambiar.
     * @param enterpriseId el ID de la empresa.
     * @throws ProductTypeNotFoundException si el tipo de producto no se encuentra.
     */
    @Override
    public void changeState(Long id, String enterpriseId) {
        ProductType productType = productTypeOutputPort.findByIdAndEnterpriseId(id, enterpriseId)
                .orElseThrow(() -> new ProductTypeNotFoundException());
        productType.setState(!productType.isState());
        productTypeOutputPort.save(productType);
    }
    
    @Override
    public List<ProductType> getProductTypesByEnterpriseIdAndState(String enterpriseId, boolean state) {
        return productTypeOutputPort.findByEnterpriseIdAndState(enterpriseId, state);
    }
    
    /**
     * Valida que el nombre de un tipo de producto sea único dentro de la empresa.
     *
     * @param productType el tipo de producto a validar.
     * @throws ProductTypeNameAlreadyExistsException si ya existe un tipo de producto con el mismo nombre.
     */
    private void validateProductTypeUniqueness(ProductType productType) {
        // El nombre ya está normalizado, se usa directamente para validación
        if (productTypeOutputPort.existsByNameAndEnterpriseId(
                productType.getName(), productType.getEnterpriseId())) {
            throw new ProductTypeNameAlreadyExistsException();
        }
    }
    
    /**
     * Valida que el nombre de un tipo de producto sea único dentro de la empresa
     * durante una actualización, excluyendo el tipo de producto que se está actualizando.
     *
     * @param id el ID del tipo de producto que se está actualizando.
     * @param productType el tipo de producto a validar.
     * @throws ProductTypeNameAlreadyExistsException si ya existe otro tipo de producto con el mismo nombre.
     */
    private void validateProductTypeUniquenessForUpdate(Long id, ProductType productType) {
        // El nombre ya está normalizado, se usa directamente para validación
        if (productTypeOutputPort.existsByNameAndEnterpriseIdAndIdNot(
                productType.getName(), productType.getEnterpriseId(), id)) {
            throw new ProductTypeNameAlreadyExistsException();
        }
    }
}
