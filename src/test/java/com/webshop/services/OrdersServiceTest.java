package com.webshop.services;

import com.webshop.models.Account;
import com.webshop.models.Cart;
import com.webshop.models.Order;
import com.webshop.models.Product;
import com.webshop.repositories.OrdersRepository;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class OrdersServiceTest {
    @MockBean
    private OrdersRepository ordersRepository;
    @Autowired
    private OrdersService ordersService;

    @BeforeEach
    public void init() {
        Account account1 = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        Account account2 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var product1 = new Product(
                "Title",
                "Description",
                25.90,
                account1
        );
        var product2 = new Product(
                "Title2",
                "Description2",
                35.90,
                account1
        );
        product1.setId(1L);
        product2.setId(2L);
        Order order1 = new Order(
                account1,
                account2,
                product1,
                new Date()
        );
        Order order2 = new Order(
                account1,
                account2,
                product2,
                new Date()
        );
        List<Order> orders = new ArrayList<>();
        orders.add(order1);
        orders.add(order2);

        when(ordersRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order order = (Order) invocation.getArguments()[0];
            orders.add(order);
            return order;
        });

        when(ordersRepository.findOrdersByBuyerId(any(Long.class))).thenAnswer(
                (i) -> orders.stream()
                        .filter(order -> order.getBuyer().getId().equals(i.getArguments()[0]))
                        .toList()
        );
    }

    @Test
    void formOrder() {
        //Arrange
        Account buyer = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        Account seller = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        Product product = new Product(
                "Title",
                "Description",
                19.99,
                seller
        );
        product.setId(3L);
        seller.setProducts(List.of(product));

        //Act
        Order order = ordersService.formOrder(buyer, product);
        order.setDate(null);

        //Assert
        Assertions.assertEquals(new Order(
                seller,
                buyer,
                product,
                null
        ), order);
    }

    @Test
    void boughtProducts() {
        //Arrange
        Account seller = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        List<Product> products = List.of(
                new Product(
                        "Title",
                        "Description",
                        25.90,
                        seller
                ),
                new Product(
                    "Title2",
                    "Description2",
                    35.90,
                        seller
                )
        );
        var buyer = new Account();
        buyer.setId(321L);
        seller.setId(123L);
        products.get(0).setId(1L);
        products.get(1).setId(2L);

        //Act
        var result = ordersService.boughtProducts(buyer);

        //Assert
        assertEquals(products, result);
    }

    @Test
    void buyCart() {
        //Arrange
        Account owner = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        Product product2 = new Product(
                "Title2",
                "Description2",
                35.90,
                new Account(
                        321L,
                        "Ivan",
                        "qwerty123",
                        "ivan.email@gmail.com",
                        null
                )
        );
        Cart cart = new Cart(2L, product2, owner);

        //Act
        ordersService.buyCart(List.of(product2), owner);
        var res = ordersService.boughtProducts(owner);

        //Assert
        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals(product2, res.get(0));

    }
}