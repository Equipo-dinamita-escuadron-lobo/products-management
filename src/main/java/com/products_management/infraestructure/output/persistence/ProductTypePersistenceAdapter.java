package com.products_management.infraestructure.output.persistence;

import com.products_management.application.ports.output.IProductTypePersistencePort;
import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;
import com.products_management.infraestructure.output.persistence.mapper.interfaces.IProductTypePersistenceMapper;
import com.products_management.infraestructure.output.persistence.repository.IProductTypeRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ProductTypePersistenceAdapter implements IProductTypePersistencePort {

    private final IProductTypeRepository productTypeRepository;
    private final IProductTypePersistenceMapper productTypePersistenceMapper;


    @Override
    public ProductType save(ProductType productType) {
        return productTypePersistenceMapper.toProductType(productTypeRepository.save(productTypePersistenceMapper.toProductTypeEntity(productType)));
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
    
    @Override
    public Optional<ProductType> findByIdAndEnterpriseId(Long id, String enterpriseId) {
        return productTypeRepository.findByIdAndEnterpriseId(id, enterpriseId).map(productTypePersistenceMapper::toProductType);
    }

    @Override
    public boolean existsByNameAndEnterpriseId(String name, String enterpriseId) {
        return productTypeRepository.existsByNameAndEnterpriseId(name, enterpriseId);
    }

    @Override
    public boolean existsByNameAndEnterpriseIdAndIdNot(String name, String enterpriseId, Long id) {
        return productTypeRepository.existsByNameAndEnterpriseIdAndIdNot(name, enterpriseId, id);
    }

    @Override
    public Page<ProductType> getAllProductTypesByWithSort(String enterpriseId, int page, int size, String sortField, String sortOrder) {
        String entitySortField = mapProductTypeSortField(sortField);
        Sort sort = "desc".equalsIgnoreCase(sortOrder) 
            ? Sort.by(entitySortField).descending() 
            : Sort.by(entitySortField).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductTypeEntity> entityPage = productTypeRepository.getProductTypesBy(enterpriseId, pageable);
        return entityPage.map(productTypePersistenceMapper::toProductType);
    }

    @Override
    public Page<ProductType> findByEnterpriseIdAndSearch(String enterpriseId, String search, int page, int size, String sortField, String sortOrder) {
        String entitySortField = mapProductTypeSortField(sortField);
        Sort sort = "desc".equalsIgnoreCase(sortOrder) 
            ? Sort.by(entitySortField).descending() 
            : Sort.by(entitySortField).ascending();
        
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductTypeEntity> entityPage = productTypeRepository.findByEnterpriseIdAndSearch(enterpriseId, search, pageable);
        return entityPage.map(productTypePersistenceMapper::toProductType);
    }

    @Override
    public long countByEnterpriseIdAndSearch(String enterpriseId, String search) {
        return productTypeRepository.countByEnterpriseIdAndSearch(enterpriseId, search);
    }

    @Override
    public long countByEnterpriseId(String enterpriseId) {
        return productTypeRepository.countByEnterpriseId(enterpriseId);
    }

    @Override
    public Page<ProductType> findActivatedByEnterpriseId(String enterpriseId, int page, int size) {
        Sort sort = Sort.by("name").ascending(); // Sort por defecto por name asc
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ProductTypeEntity> entityPage = productTypeRepository.findActivatedByEnterpriseId(enterpriseId, pageable);
        return entityPage.map(productTypePersistenceMapper::toProductType);
    }

    @Override
    public long countActivatedByEnterpriseId(String enterpriseId) {
        return productTypeRepository.countActivatedByEnterpriseId(enterpriseId);
    }

    /**
     * Mapea el campo de ordenamiento del modelo ProductType al campo correspondiente en ProductTypeEntity.
     * Solo permite ordenamiento por nombre.
     * @param sortField Campo de ordenamiento del modelo
     * @return Campo de ordenamiento de la entidad
     */
    private String mapProductTypeSortField(String sortField) {
        if (sortField == null || sortField.trim().isEmpty()) {
            return "name"; // Default
        }
        if ("name".equalsIgnoreCase(sortField)) {
            return "name";
        }
        return "name"; // Default para cualquier otro campo
    }
}
