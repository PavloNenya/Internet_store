package com.webshop.services;

import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.models.Account;
import com.webshop.models.Order;
import com.webshop.models.Product;
import com.webshop.repositories.OrdersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class OrdersService {
    private final OrdersRepository ordersRepository;

    public Order formOrder(Account buyer, Account seller, Product product) {
        if(!product.isActive()) {
            throw new ProductNotFoundException();
        }
        var order = Order.builder()
                .sum(product.getPrice())
                .buyer(buyer)
                .seller(seller)
                .date(new Date())
                .product(product)
                .build();
        return ordersRepository.save(order);
    }

    public List<Product> boughtProducts(Long accountId) {
        return StreamSupport.stream(ordersRepository.findOrdersByBuyerId(accountId).spliterator(), false)
                .map(Order::getProduct)
                .toList();
    }

    public void buyCart(List<Product> products, Account buyer) {
        products.forEach(product -> formOrder(buyer, product.getSeller(), product));
    }
}
