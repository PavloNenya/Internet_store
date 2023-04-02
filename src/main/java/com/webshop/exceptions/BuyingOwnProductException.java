package com.webshop.exceptions;

public class BuyingOwnProductException extends RuntimeException {
    public BuyingOwnProductException() {
        super("You can't buy your own product!");
    }

}
