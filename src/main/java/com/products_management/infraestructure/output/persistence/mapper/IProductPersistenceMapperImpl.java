package com.products_management.infraestructure.output.persistence.mapper;

import com.products_management.domain.model.Product;
import com.products_management.infraestructure.output.persistence.entity.ProductEntity;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Component;


@Component
public class IProductPersistenceMapperImpl implements IProductPersistenceMapper {

    @Override
    public ProductEntity toProductEntity(Product product) {
        if ( product == null ) {
            return null;
        }

        ProductEntity productEntity = new ProductEntity();

        productEntity.setCategory( product.getCategory() );
        productEntity.setCode( product.getCode() );
        productEntity.setCreationDate( product.getCreationDate() );
        productEntity.setDescription( product.getDescription() );
        productEntity.setId( product.getId() );
        productEntity.setItemType( product.getItemType() );
        productEntity.setMaxQuantity( product.getMaxQuantity() );
        productEntity.setMinQuantity( product.getMinQuantity() );
        productEntity.setPrice( product.getPrice() );
        productEntity.setSupplier( product.getSupplier() );
        productEntity.setTaxPercentage( product.getTaxPercentage() );
        productEntity.setUnitOfMeasure( product.getUnitOfMeasure() );
        productEntity.setState("true");
        

        return productEntity;
    }

    @Override
    public Product toProduct(ProductEntity productEntity) {
        if ( productEntity == null ) {
            return null;
        }

        Product.ProductBuilder product = Product.builder();

        product.category( productEntity.getCategory() );
        product.code( productEntity.getCode() );
        product.creationDate( productEntity.getCreationDate() );
        product.description( productEntity.getDescription() );
        product.id( productEntity.getId() );
        product.itemType( productEntity.getItemType() );
        product.maxQuantity( productEntity.getMaxQuantity() );
        product.minQuantity( productEntity.getMinQuantity() );
        product.price( productEntity.getPrice() );
        product.supplier( productEntity.getSupplier() );
        product.taxPercentage( productEntity.getTaxPercentage() );
        product.unitOfMeasure( productEntity.getUnitOfMeasure() );

        return product.build();
    }

    @Override
    public List<Product> toProductList(List<ProductEntity> productList) {
        if ( productList == null ) {
            return null;
        }

        List<Product> list = new ArrayList<Product>( productList.size() );
        for ( ProductEntity productEntity : productList ) {
            list.add( toProduct( productEntity ) );
        }

        return list;
    }
}
