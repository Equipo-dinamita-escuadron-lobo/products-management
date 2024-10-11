package com.products_management.application.service;

import com.products_management.application.ports.input.IProductTypeServicePort;
import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.domain.model.ProductType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductTypeService implements IProductTypeServicePort {

    private final IProductTypePersistencePort productTypeOutputPort;

    public ProductTypeService(IProductTypePersistencePort productTypeOutputPort) {
        this.productTypeOutputPort = productTypeOutputPort;
    }

    @Override
    public ProductType createProductType(ProductType productType) {
        return productTypeOutputPort.save(productType);
    }

    @Override
    public List<ProductType> getProductTypesByEnterpriseId(String enterpriseId) {
        return productTypeOutputPort.findByEnterpriseId(enterpriseId);
    }

    @Override
    public List<ProductType> listAllProductTypes() {
        return productTypeOutputPort.findAll();
    }

    @Override
    public ProductType updateProductType(Long id, ProductType productType) {
        return productTypeOutputPort.update(id, productType);
    }

    @Override
    public void deleteProductType(Long id) {
        productTypeOutputPort.delete(id);
    }

    @Override
    public Optional<ProductType> findById(Long id) {
        return productTypeOutputPort.findById(id);
    }
}
