package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.products_management.application.dto.ProductSyncDto;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;

@Mapper(componentModel = "spring")
public interface IProductSyncMapper {
    @Mapping(target = "productId", source = "id")
    ProductSyncDto toDto(ProductEntity productEntity);

    List<ProductSyncDto> toDto(List<ProductEntity> productEntities);
}
