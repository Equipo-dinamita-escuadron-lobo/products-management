package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.input.rest.dto.request.ProductTypeRequest;
import com.products_management.infraestructure.input.rest.dto.response.ProductTypeResponse;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Interfaz para mapear entre modelos de entrada (ProductTypeRequest) y objetos del dominio (ProductType).
 */
@Mapper(componentModel = "spring")
public interface IProductTypeRestMapper {

    /**
     * Convierte un objeto ProductTypeRequest en un ProductType del dominio.
     * @param productTypeRequest Objeto de solicitud.
     * @return ProductType correspondiente.
     */
    @Mapping(target = "id", ignore = true)
    ProductType toProductType(ProductTypeRequest productTypeRequest);

    /**
     * Convierte una lista de ProductTypeRequest en una lista de objetos ProductType del dominio.
     * @param productTypeRequestList Lista de objetos de solicitud.
     * @return Lista de objetos ProductType correspondientes.
     */
    ProductTypeResponse toProductTypeResponse(ProductType productType);
    List<ProductType> toProductTypeList(List<ProductTypeRequest> productTypeRequestList);
}
