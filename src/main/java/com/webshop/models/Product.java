package com.webshop.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String title, description;
    private double price;

    public Products(String title, String description, double price) {
        this.title = title;
        this.description = description;
        this.price = price;
    }

    protected Products() {

    }
}
