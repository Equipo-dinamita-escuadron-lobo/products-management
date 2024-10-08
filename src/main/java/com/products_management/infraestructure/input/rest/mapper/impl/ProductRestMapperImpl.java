package com.products_management.infraestructure.input.rest.mapper.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;

/**
 * Implementación de {@link IProductRestMapper} que realiza la conversión entre entidades de producto y sus representaciones REST.
 */
@Component
public class ProductRestMapperImpl implements IProductRestMapper {

    /**
     * Convierte una solicitud de creación de producto ({@link ProductCreateRequest}) en una entidad de producto ({@link Product}).
     *
     * @param productCreateRequest la solicitud de creación de producto.
     * @return la entidad de producto convertida.
     */
    @Override
    public Product toProduct(ProductCreateRequest productCreateRequest) {
        if (productCreateRequest == null) {
            return null;
        }

        Product.ProductBuilder productBuilder = Product.builder();
        productBuilder.id(productCreateRequest.getId());
        productBuilder.code(productCreateRequest.getCode());
        productBuilder.itemType(productCreateRequest.getItemType());
        productBuilder.description(productCreateRequest.getDescription());
        productBuilder.minQuantity(productCreateRequest.getMinQuantity());
        productBuilder.maxQuantity(productCreateRequest.getMaxQuantity());
        productBuilder.taxPercentage(productCreateRequest.getTaxPercentage());
        productBuilder.creationDate(productCreateRequest.getCreationDate());
        productBuilder.unitOfMeasureId(productCreateRequest.getUnitOfMeasureId());
        productBuilder.categoryId(productCreateRequest.getCategoryId());
        productBuilder.enterpriseId(productCreateRequest.getEnterpriseId());
        productBuilder.price(productCreateRequest.getPrice());
        productBuilder.state(productCreateRequest.getState());
        productBuilder.reference(productCreateRequest.getReference());

        return productBuilder.build();
    }

    /**
     * Convierte una entidad de producto ({@link Product}) en una respuesta de producto ({@link ProductResponse}).
     *
     * @param product la entidad de producto.
     * @return la respuesta de producto convertida.
     */
    @Override
    public ProductResponse toProductResponse(Product product) {
        if (product == null) {
            return null;
        }

        product.generateCode();

        ProductResponse.ProductResponseBuilder productResponseBuilder = ProductResponse.builder();
        productResponseBuilder.id(product.getId());
        productResponseBuilder.code(product.getCode());
        productResponseBuilder.itemType(product.getItemType());
        productResponseBuilder.description(product.getDescription());
        productResponseBuilder.minQuantity(product.getMinQuantity());
        productResponseBuilder.maxQuantity(product.getMaxQuantity());
        productResponseBuilder.taxPercentage(product.getTaxPercentage());
        productResponseBuilder.creationDate(product.getCreationDate());
        productResponseBuilder.unitOfMeasureId(product.getUnitOfMeasureId());
        productResponseBuilder.categoryId(product.getCategoryId());
        productResponseBuilder.enterpriseId(product.getEnterpriseId());
        productResponseBuilder.price(product.getPrice());
        productResponseBuilder.state(product.getState());
        productResponseBuilder.reference(product.getReference());

        return productResponseBuilder.build();
    }

    /**
     * Convierte una lista de entidades de producto ({@link Product}) en una lista de respuestas de producto ({@link ProductResponse}).
     *
     * @param productList la lista de entidades de producto.
     * @return la lista de respuestas de producto convertida.
     */
    @Override
    public List<ProductResponse> toProductResponseList(List<Product> productList) {
        if (productList == null) {
            return null;
        }

        List<ProductResponse> productResponseList = new ArrayList<>(productList.size());
        for (Product product : productList) {
            productResponseList.add(toProductResponse(product));
        }

        return productResponseList;
    }
}
