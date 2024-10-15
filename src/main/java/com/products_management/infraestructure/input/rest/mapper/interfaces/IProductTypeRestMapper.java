package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.input.rest.model.request.ProductTypeRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductTypeResponse;

import java.util.List;

/**
 * Interfaz para mapear entre modelos de entrada (ProductTypeRequest) y objetos del dominio (ProductType).
 */
public interface IProductTypeRestMapper {

    /**
     * Convierte un objeto ProductTypeRequest en un ProductType del dominio.
     * @param productTypeRequest Objeto de solicitud.
     * @return ProductType correspondiente.
     */
    ProductType toProductType(ProductTypeRequest productTypeRequest);

    /**
     * Convierte una lista de ProductTypeRequest en una lista de objetos ProductType del dominio.
     * @param productTypeRequestList Lista de objetos de solicitud.
     * @return Lista de objetos ProductType correspondientes.
     */
    ProductTypeResponse toProductTypeResponse(ProductType productType);
    List<ProductType> toProductTypeList(List<ProductTypeRequest> productTypeRequestList);
}
