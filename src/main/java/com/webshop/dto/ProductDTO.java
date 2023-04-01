package com.webshop.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductDTO {
    private String title, description;
    private double price;
    private Long sellerId;
    private Long id;

    public ProductDTO(String title, String description, double price, Long id) {
        this.title = title;
        this.description = description;
        this.price = price;
        this.id = id;
    }
}
