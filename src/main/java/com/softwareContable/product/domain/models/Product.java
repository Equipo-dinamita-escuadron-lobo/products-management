package com.softwareContable.product.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
@Setter
@Getter
@Entity
@Table(name="product")
public class Product {
    private String id;
    private String name;
    private float price;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;
    private String description;

    public Product(String id, String name, float price, LocalDateTime dateCreated, LocalDateTime dateUpdated, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.description = description;
    }


}