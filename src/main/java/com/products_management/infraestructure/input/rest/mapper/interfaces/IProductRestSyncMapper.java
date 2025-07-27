package com.products_management.infraestructure.input.rest.mapper.interfaces;

import java.util.List;

import org.mapstruct.Mapper;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.model.response.ProductSyncDto;

@Mapper(componentModel = "spring")
public interface IProductRestSyncMapper {

    List<ProductSyncDto> toProductSyncDto(List<Product> productResponse);
}
