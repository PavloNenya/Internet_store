package com.webshop.repositories;

import com.webshop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface OrdersRepository extends JpaRepository<Order, Long> {
    Iterable<Order> findOrdersByBuyerId(Long id);
}
