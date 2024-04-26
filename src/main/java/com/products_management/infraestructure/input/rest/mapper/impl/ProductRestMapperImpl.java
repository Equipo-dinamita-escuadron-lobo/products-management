package com.products_management.infraestructure.input.rest.mapper.impl;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.mapper.interfaces.IProductRestMapper;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ProductRestMapperImpl implements IProductRestMapper {

    @Override
    public Product toProduct(ProductCreateRequest productCreateRequest) {
        if ( productCreateRequest == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();
        
        product.id( productCreateRequest.getId() );
        product.code(productCreateRequest.getCode());
        product.itemType(productCreateRequest.getItemType());
        product.description( productCreateRequest.getDescription() );
        product.minQuantity( productCreateRequest.getMinQuantity() );
        product.maxQuantity(productCreateRequest.getMaxQuantity());
        product.taxPercentage( productCreateRequest.getTaxPercentage() );
        product.creationDate(productCreateRequest.getCreationDate());
        product.unitOfMeasureId( productCreateRequest.getUnitOfMeasureId() );
        product.supplierId(productCreateRequest.getSupplierId());
        product.categoryId(productCreateRequest.getCategoryId());
        product.enterpriseId(productCreateRequest.getEnterpriseId());
        product.price(productCreateRequest.getPrice());
        product.state(productCreateRequest.getState());
        
        return product.build();
    }

    @Override
    public ProductResponse toProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.id( product.getId() );
        productResponse.code(product.getCode());
        productResponse.itemType(product.getItemType());
        productResponse.description(product.getDescription());
        productResponse.minQuantity(product.getMinQuantity());
        productResponse.maxQuantity( product.getMaxQuantity() );
        productResponse.taxPercentage(product.getTaxPercentage());
        productResponse.creationDate(product.getCreationDate());
        productResponse.unitOfMeasureId(product.getUnitOfMeasureId());
        productResponse.supplierId(product.getSupplierId());
        productResponse.categoryId(product.getCategoryId());
        productResponse.enterpriseId(product.getEnterpriseId());
        productResponse.price(product.getPrice());
        productResponse.state(product.getState());
        
        return productResponse.build();
    }

    @Override
    public List<ProductResponse> toProductResponseList(List<Product> productList) {
        if ( productList == null ) {
            return null;
        }

        List<ProductResponse> list = new ArrayList<ProductResponse>( productList.size() );
        for ( Product product : productList ) {
            list.add( toProductResponse( product ) );
        }

        return list;
    }
}
