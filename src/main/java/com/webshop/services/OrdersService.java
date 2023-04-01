package com.webshop.services;

import com.webshop.models.Account;
import com.webshop.models.Order;
import com.webshop.models.Product;
import com.webshop.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public Order formOrder(Account buyer, Account seller, Product product, Double sum) {
        return ordersRepository.save(
                Order.builder()
                        .sum(product.getPrice())
                        .buyer(buyer)
                        .seller(seller)
                        .date(new Date())
                        .product(product)
                        .sum(sum)
                        .build()
        );
    }

    public List<Product> boughtProducts(Long accountId) {
        return StreamSupport.stream(ordersRepository.findOrdersBySellerId(accountId).spliterator(), false)
                .map(Order::getProduct)
                .toList();
    }
}
