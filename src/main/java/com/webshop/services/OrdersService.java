package com.webshop.services;

import com.webshop.models.Order;
import com.webshop.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.time.LocalDate;

@Service
@AllArgsConstructor
public class OrderService {
    OrderRepository orderRepository;

    public Order formOrder(int productId, int clientId, int quantity) {
        return orderRepository.save(new Order(productId, clientId, quantity, new Date()));
    }
}
