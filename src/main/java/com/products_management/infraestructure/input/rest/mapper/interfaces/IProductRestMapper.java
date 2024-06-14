package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.domain.model.Product;

import java.util.List;

/**
 * Interfaz que define métodos para mapear entre entidades de producto y sus representaciones REST.
 */
public interface IProductRestMapper {

    /**
     * Convierte una solicitud de creación de producto ({@link ProductCreateRequest}) en una entidad de producto ({@link Product}).
     *
     * @param productCreateRequest la solicitud de creación de producto.
     * @return la entidad de producto convertida.
     */
    Product toProduct(ProductCreateRequest productCreateRequest);

    /**
     * Convierte una entidad de producto ({@link Product}) en una respuesta de producto ({@link ProductResponse}).
     *
     * @param product la entidad de producto.
     * @return la respuesta de producto convertida.
     */
    ProductResponse toProductResponse(Product product);

    /**
     * Convierte una lista de entidades de producto ({@link Product}) en una lista de respuestas de producto ({@link ProductResponse}).
     *
     * @param productList la lista de entidades de producto.
     * @return la lista de respuestas de producto convertida.
     */
    List<ProductResponse> toProductResponseList(List<Product> productList);
}
