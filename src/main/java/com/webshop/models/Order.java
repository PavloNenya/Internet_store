package com.webshop.models;

import jakarta.persistence.*;
import lombok.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;

@Entity(name = "orders")
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    private Account seller;
    @OneToOne(fetch = FetchType.LAZY)
    private Account buyer;
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private Product product;
    private Date date;
    private Double sum;

    public Order(Account seller, Account buyer, Product product, Date date, Double sum) {
        this.seller = seller;
        this.buyer = buyer;
        this.product = product;
        this.date = date;
        this.sum = sum;
    }
}
