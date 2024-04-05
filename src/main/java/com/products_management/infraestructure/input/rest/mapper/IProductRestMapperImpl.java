package com.products_management.infraestructure.input.rest.mapper;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class IProductRestMapperImpl implements IProductRestMapper {

    @Override
    public Product toProduct(ProductCreateRequest productCreateRequest) {
        if ( productCreateRequest == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.category( productCreateRequest.getCategory() );
        product.code( productCreateRequest.getCode() );
        product.creationDate( productCreateRequest.getCreationDate() );
        product.description( productCreateRequest.getDescription() );
        product.itemType( productCreateRequest.getItemType() );
        product.maxQuantity( productCreateRequest.getMaxQuantity() );
        product.minQuantity( productCreateRequest.getMinQuantity() );
        product.price( productCreateRequest.getPrice() );
        product.supplier( productCreateRequest.getSupplier() );
        product.taxPercentage( productCreateRequest.getTaxPercentage() );
        product.unitOfMeasure( productCreateRequest.getUnitOfMeasure() );

        return product.build();
    }

    @Override
    public ProductResponse toProductResponse(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductResponse.ProductResponseBuilder productResponse = ProductResponse.builder();

        productResponse.category( product.getCategory() );
        productResponse.code( product.getCode() );
        productResponse.creationDate( product.getCreationDate() );
        productResponse.description( product.getDescription() );
        productResponse.id( product.getId() );
        productResponse.itemType( product.getItemType() );
        productResponse.maxQuantity( product.getMaxQuantity() );
        productResponse.minQuantity( product.getMinQuantity() );
        productResponse.price( product.getPrice() );
        productResponse.supplier( product.getSupplier() );
        productResponse.taxPercentage( product.getTaxPercentage() );
        productResponse.unitOfMeasure( product.getUnitOfMeasure() );

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
