package com.products_management.infraestructure.output.persistence.mapper.interfaces;

import com.products_management.domain.model.ProductType;
import com.products_management.infraestructure.output.persistence.entity.ProductTypeEntity;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

/**
 * Interfaz para mapear entre entidades de persistencia (ProductTypeEntity) y objetos del dominio (ProductType).
 */
public interface IProductTypePersistenceMapper {

    /**
     * Convierte un objeto ProductType del dominio en un ProductTypeEntity de persistencia.
     * @param productType Objeto ProductType del dominio.
     * @return ProductTypeEntity correspondiente.
     */
    @Operation(summary = "Convierte un objeto ProductType del dominio en un ProductTypeEntity de persistencia")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Objeto ProductType del dominio convertido"),
        @ApiResponse(responseCode = "404", description = "Objeto ProductType del dominio no convertido")
    })
    @Parameter(description = "Objeto ProductType del dominio")
    ProductTypeEntity toProductTypeEntity(ProductType productType);

    /**
     * Convierte un ProductTypeEntity de persistencia en un objeto ProductType del dominio.
     * @param productTypeEntity ProductTypeEntity de persistencia.
     * @return Objeto ProductType correspondiente.
     */
    @Operation(summary = "Convierte un ProductTypeEntity de persistencia en un objeto ProductType del dominio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "ProductTypeEntity de persistencia convertida"),
        @ApiResponse(responseCode = "404", description = "ProductTypeEntity de persistencia no convertida")
    })
    @Parameter(description = "ProductTypeEntity de persistencia")
    ProductType toProductType(ProductTypeEntity productTypeEntity);

    /**
     * Convierte una lista de ProductTypeEntity de persistencia en una lista de objetos ProductType del dominio.
     * @param productTypeEntityList Lista de ProductTypeEntity de persistencia.
     * @return Lista de objetos ProductType correspondiente.
     */
    @Operation(summary = "Convierte una lista de ProductTypeEntity de persistencia en una lista de objetos ProductType del dominio")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ProductTypeEntity de persistencia convertida"),
        @ApiResponse(responseCode = "404", description = "Lista de ProductTypeEntity de persistencia no convertida")
    })
    @Parameter(description = "Lista de ProductTypeEntity de persistencia")
    List<ProductType> toProductTypeList(List<ProductTypeEntity> productTypeEntityList);
}
