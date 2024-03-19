package com.products.infraestructure.input.Rest.Mapper;

import com.products.infraestructure.input.Rest.Data.Request.ProductRequest;
import org.mapstruct.Mapper;

import com.products.domain.models.Product;
import com.products.infraestructure.input.Rest.Data.Response.ProductResponse;

@Mapper
public interface IProductRestMapper {

    ProductResponse toResponse(Product product);

    Product toProduct(ProductRequest productRequest);

}
