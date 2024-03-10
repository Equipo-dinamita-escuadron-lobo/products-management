package domian.models;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDateTime;
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

    public Product(String name, float price, LocalDateTime dateCreated, LocalDateTime dateUpdated, String description) {
        this.name = name;
        this.price = price;
        this.dateCreated = dateCreated;
        this.dateUpdated = dateUpdated;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public LocalDateTime getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }

    public LocalDateTime getDateUpdated() {
        return dateUpdated;
    }

    public void setDateUpdated(LocalDateTime dateUpdated) {
        this.dateUpdated = dateUpdated;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}