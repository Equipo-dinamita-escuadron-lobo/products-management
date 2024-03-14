package com.products.infraestructure.input.Rest.Mapper;

import org.mapstruct.Mapper;

import com.products.domain.models.Product;
import com.products.infraestructure.input.Rest.Data.Request.ProductCreateRequest;
import com.products.infraestructure.input.Rest.Data.Response.ProductCreateResponse;
import com.products.infraestructure.input.Rest.Data.Response.ProductGetByIdResponse;

@Mapper
public interface IProductRestMapper {

    public ProductGetByIdResponse toResponse(Product product);
    public Product toDomain(ProductGetByIdResponse productGetByIdDto);  
    
    Product toProduct(ProductCreateRequest productCreateRequest);
    ProductCreateRequest toProductCreate(Product product);  

    ProductCreateResponse toCreateProductResponse(Product product); 


}
