package com.webshop.exceptions;

public class IncorrectProductDataException extends RuntimeException{
    public IncorrectProductDataException() {
        super("Incorrect product data!");
    }
}
