package com.webshop.repositories;

import com.webshop.models.Order;
import org.springframework.data.repository.CrudRepository;


public interface OrderRepository extends CrudRepository<Order, Long> {
}
