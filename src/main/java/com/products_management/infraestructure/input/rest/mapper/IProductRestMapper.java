package com.products_management.infraestructure.input.rest.mapper;

import com.products_management.infraestructure.input.rest.model.response.ProductResponse;
import org.mapstruct.Mapper;
import com.products_management.infraestructure.input.rest.model.request.ProductCreateRequest;
import com.products_management.domain.model.Product;
import org.mapstruct.ReportingPolicy;
import java.util.List;

@Mapper (componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface IProductRestMapper {

    Product toProduct(ProductCreateRequest productCreateRequest);
    ProductResponse toProductResponse(Product product);
    List<ProductResponse> toProductResponseList(List<Product> productList);


}
