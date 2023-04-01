package com.webshop.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Entity(name = "products")
@Data
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title, description;
    private double price;
    @NonNull
    @ManyToOne(
            cascade = CascadeType.REMOVE,
            fetch = FetchType.LAZY
    )
    @JoinColumn
    private Account seller;
    private boolean isActive = true;

    public Product(String title, String description, double price, Account seller) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.seller = seller;
    }

}
