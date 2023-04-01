package com.webshop.repositories;

import com.webshop.dto.OrderDTO;
import com.webshop.models.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface OrdersRepository extends JpaRepository<Order, Long> {
//    @Query(value = "SELECT accounts.id AS sellerId, accounts.id AS buyerId, products.id AS productId FROM")
//
//    public OrderDTO findOrderDetails();
    Iterable<Order> findOrdersBySellerId(Long id);
}
