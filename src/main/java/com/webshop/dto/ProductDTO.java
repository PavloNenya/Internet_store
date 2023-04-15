package com.webshop.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class ProductDTO {
    @NonNull
    private String title, description;
    @NonNull
    private double price;
    @NonNull
    private Long sellerId;
    @NonNull
    private Long id;
}
