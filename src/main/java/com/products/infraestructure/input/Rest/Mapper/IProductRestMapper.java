package com.products.infraestructure.input.Rest.Mapper;

import com.products.infraestructure.ouput.jpaAdapter.Entity.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.products.domain.models.Product;
import com.products.infraestructure.input.Rest.Data.Request.ProductRequest;
import com.products.infraestructure.input.Rest.Data.Response.ProductResponse;

import java.util.List;

@Mapper
public interface IProductRestMapper {

    ProductResponse toResponse(Product product);

    Product toProduct(ProductRequest productRequest);

    List<ProductResponse> toProductResponseList(List<Product> productList);
}
