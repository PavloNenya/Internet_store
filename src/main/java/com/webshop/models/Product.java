package com.webshop.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;

@Entity(name = "products")
@Getter
@Setter
@RequiredArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String title, description;
    @NonNull
    private double price;
    @NonNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Account seller;
    private boolean isActive = true;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Double.compare(product.getPrice(), price) == 0
                && isActive == product.isActive()
                && id.equals(product.getId())
                && Objects.equals(title, product.getTitle())
                && Objects.equals(description, product.getDescription())
                && seller.getId().equals(product.getSeller().getId());
    }

//    @Override
//    public int hashCode() {
//        return Objects.hash(id, title, description, price, seller, isActive);
//    }
}
