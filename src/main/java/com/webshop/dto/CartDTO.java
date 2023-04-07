package com.webshop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartDTO {
    Long productId;
    Long accountId;
}
