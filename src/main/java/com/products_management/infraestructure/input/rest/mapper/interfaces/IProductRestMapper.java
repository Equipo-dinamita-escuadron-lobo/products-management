package com.products_management.infraestructure.input.rest.mapper.interfaces;

import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.domain.model.Product;
import java.util.List;

public interface IProductRestMapper {

    Product toProduct(ProductCreateRequest productCreateRequest);
    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toProductResponseList(List<Product> productList);
}
