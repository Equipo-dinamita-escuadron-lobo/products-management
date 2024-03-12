package com.softwareContable.product.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "category")
public class Category {
    private String id;
    private String name;
    private String description;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public Category(String id, String name, String description, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

}
