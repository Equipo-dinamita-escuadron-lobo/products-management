package com.products.infraestructure.ouput.jpaAdapter.Entity;
import jakarta.annotation.Generated;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String itemType;
    private String code;
    private String description;
    private Integer minQuantity;
    private Integer maxQuantity;
    private Integer taxPercentage;
    private Date creationDate;
    private String unitOfMeasure;//TODO cambiar tipo de dato por unidad de medida
    private String supplier; //TODO cambiar tipo de dato por lista de proveedores
    private String category; //TODO cambiar tipo de dato por lista de categorias
    private double price;
    
}
