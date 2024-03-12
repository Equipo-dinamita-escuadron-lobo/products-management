package com.softwareContable.product.domain.models;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "unit")
public class Unit {
    private String id;
    private String name;
    private String abbreviation;
    private LocalDateTime dateCreated;
    private LocalDateTime dateUpdated;

    public Unit(String id, String name, String abbreviation, LocalDateTime dateCreated, LocalDateTime dateUpdated) {
        this.id = id;
        this.name = name;
        this.abbreviation = abbreviation;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
    }

}
