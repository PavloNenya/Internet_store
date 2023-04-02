package com.webshop.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductDTO {
    private String title, description;
    private double price;
    private Long sellerId;
    private Long id;
}
