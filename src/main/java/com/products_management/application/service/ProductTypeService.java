package com.products_management.application.service;

import com.products_management.application.ports.input.IProductServicePort;
import com.products_management.application.ports.input.IProductTypeServicePort;
import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.domain.exception.ProductTypeAssociatedException;
import com.products_management.domain.exception.ProductTypeNotFoundException;
import com.products_management.domain.model.Product;
import com.products_management.domain.model.ProductType;
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
        // Establecer estado activo por defecto si no se especifica
        if (productType.getId() == null) { // Solo para nuevos productos
            productType.setState(true);
        }
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
    public List<ProductType> listAllProductTypes() {
        return productTypeOutputPort.findAll();
    }

    @Override
    public ProductType updateProductType(Long id, ProductType productType) {
        // Verificar que el tipo de producto existe antes de actualizar
        Optional<ProductType> existingProductType = productTypeOutputPort.findById(id);
        if (existingProductType.isEmpty()) {
            throw new ProductTypeNotFoundException();
        }
        return productTypeOutputPort.update(id, productType);
    }

    @Override
    public void deleteProductType(Long id) {
        // Verificar que el tipo de producto existe antes de eliminar
        Optional<ProductType> existingProductType = productTypeOutputPort.findById(id);
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
     * Busca un tipo de producto por ID y lanza excepción si no se encuentra.
     * 
     * @param id el ID del tipo de producto a buscar
     * @return el tipo de producto encontrado
     * @throws ProductTypeNotFoundException si no se encuentra el tipo de producto
     */
    public ProductType getProductTypeById(Long id) {
        return productTypeOutputPort.findById(id)
                .orElseThrow(() -> new ProductTypeNotFoundException());
    }
    
    /**
     * Cambia el estado de un tipo de producto (activado/desactivado).
     *
     * @param id el ID del tipo de producto cuyo estado se va a cambiar.
     * @throws ProductTypeNotFoundException si el tipo de producto no se encuentra.
     */
    @Override
    public void changeState(Long id) {
        ProductType productType = productTypeOutputPort.findById(id)
                .orElseThrow(() -> new ProductTypeNotFoundException());
        productType.setState(!productType.isState());
        productTypeOutputPort.save(productType);
    }
    
    @Override
    public List<ProductType> getProductTypesByEnterpriseIdAndState(String enterpriseId, boolean state) {
        return productTypeOutputPort.findByEnterpriseIdAndState(enterpriseId, state);
    }
}
