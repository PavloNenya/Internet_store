package com.webshop.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Product not found!");
    }

    public ProductNotFoundException(Long id) {
        super("Product " + id + " not found!");
    }
}
