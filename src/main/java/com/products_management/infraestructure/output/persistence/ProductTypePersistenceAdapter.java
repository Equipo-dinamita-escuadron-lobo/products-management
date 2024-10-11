package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.output.persistence.mapper.impl.ProductTypePersistenceMapperImpl;
import com.products_management.infraestructure.output.persistence.repository.IProductTypeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductTypePersistenceAdapter implements IProductTypePersistencePort {

    private final IProductTypeRepository productTypeRepository;
    private final ProductTypePersistenceMapperImpl productTypePersistenceMapper;


    @Override
    public ProductType save(ProductType productType) {
        return productTypePersistenceMapper.toProductType(productTypeRepository.save(productTypePersistenceMapper.toProductTypeEntity(productType)));
    }

    @Override
    public List<ProductType> findByEnterpriseId(Long enterpriseId) {
        return productTypePersistenceMapper.toProductTypeList(productTypeRepository.findByEnterpriseId(enterpriseId));
    }

    @Override
    public List<ProductType> findAll() {
        return productTypePersistenceMapper.toProductTypeList(productTypeRepository.findAll());
    }

    @Override
    public ProductType update(Long id, ProductType productType) {
        productType.setId(id);
        return productTypePersistenceMapper.toProductType(productTypeRepository.save(productTypePersistenceMapper.toProductTypeEntity(productType)));
    }

    @Override
    public void delete(Long id) {
        productTypeRepository.deleteById(id);
    }

    @Override
    public Optional<ProductType> findById(Long id) {
        return productTypeRepository.findById(id).map(productTypePersistenceMapper::toProductType);

    }
}
