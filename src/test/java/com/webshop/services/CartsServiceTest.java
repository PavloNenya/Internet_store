package com.webshop.services;

import com.webshop.models.Account;
import com.webshop.models.Cart;
import com.webshop.models.Product;
import com.webshop.repositories.CartsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CartsServiceTest {
    @MockBean
    private CartsRepository cartsRepository;

    @Autowired
    private CartsService cartsService;

    @BeforeEach
    void init() {
        Account seller1 = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        Account seller2 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        Product product1 = new Product(
                "Title",
                "Description",
                25.90,
                seller1
        );
        Product product2 = new Product(
                        "Title2",
                        "Description2",
                        35.90,
                        seller2
        );
        product1.setId(1L);
        product2.setId(2L);
        Cart cart1 = new Cart(1L, product1, seller2);
        Cart cart2 = new Cart(2L, product2, seller1);
        when(cartsRepository.findCartsByOwnerId(321L)).thenAnswer(invocation -> List.of(cart1));
        when(cartsRepository.findCartsByOwnerId(123L)).thenAnswer(invocation -> List.of(cart2));
        when(cartsRepository.save(any(Cart.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        doAnswer(invocation -> cart1).when(cartsRepository).deleteCartByOwnerIdAndProductId(321L, 1L);
        doAnswer(invocation -> {
            System.out.println("deleting cart2");
            cart2.setOwner(null);
            cart2.setProduct(null);
            cart2.setId(null);
            return cart2;
        }).when(cartsRepository).deleteCartByOwnerIdAndProductId(123L, 2L);
        doAnswer(invocation -> {
            System.out.println("deleting cart1");
            cart1.setOwner(null);
            cart1.setProduct(null);
            cart1.setId(null);
            return cart1;
        }).when(cartsRepository).deleteCartByOwnerIdAndProductId(321L, 1L);
        when(cartsRepository.findByProductIdAndOwnerId(2L, 123L)).thenReturn(Optional.of(cart2));
        when(cartsRepository.findByProductIdAndOwnerId(1L, 321L)).thenReturn(Optional.of(cart1));
        doAnswer(invocation -> {
            cartsRepository.findCartsByOwnerId(((Account)invocation.getArguments()[0]).getId())
                    .forEach((cart) -> {
                        cart.setOwner(null);
                        cart.setProduct(null);
                        cart.setId(null);
                    });
            System.out.println("cart deleted");
            return null;
        }).when(cartsRepository).deleteAllByOwner(any(Account.class));
    }

    @Test
    void findCartList() {
        //Arrange
        Account seller1 = new Account(
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
        product2.setId(2L);
        Cart cart = new Cart(2L, product2, seller1);

        //Act
        var res = cartsService.findOwnersCartList(seller1.getId());

        //Assert
        Assertions.assertEquals(1, res.size());
        Assertions.assertEquals(cart, res.get(0));
    }

    @Test
    void saveCart() {
        //Arrange
        Account seller1 = new Account(
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
        Product product3 = new Product(
                "Test title",
                "Test description",
                35.90,
                new Account(
                        321L,
                        "Ivan",
                        "qwerty123",
                        "ivan.email@gmail.com",
                        null
                )
        );
        product2.setId(2L);
        product3.setId(3L);
        Cart cart = new Cart(2L, product2, seller1);
        Cart cart1 = new Cart(2L, product3, seller1);

        //Act
        var res1 = cartsService.saveCart(cart);
        var res2 = cartsService.saveCart(cart1);

        //Assert
        Assertions.assertNull(res1);
        Assertions.assertEquals(cart1, res2);
    }

    @Test
    void deleteFromCart() {
        //Arrange
        Account buyer = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        Account seller2 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
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
        product2.setId(2L);

        //Act
        cartsService.deleteFromCart(buyer, product2);
        var res = cartsService.findOwnersCartList(buyer.getId());

        //Assert
        Assertions.assertNull(res.get(0).getProduct());
        Assertions.assertNull(res.get(0).getId());
        Assertions.assertNull(res.get(0).getOwner());
    }

    @Test
    void isProductInCartByOwner() {
        //Arrange
        Account owner = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );
        Product product1 = new Product(
                "Title1",
                "Description1",
                35.90,
                new Account(
                        321L,
                        "Ivan",
                        "qwerty123",
                        "ivan.email@gmail.com",
                        null
                )
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
        product2.setId(2L);
        product1.setId(1L);

        //Act
        var res1 = cartsService.isProductInCartOfOwner(product2, owner);
        var res2 = cartsService.isProductInCartOfOwner(product1, owner);

        //Assert
        Assertions.assertTrue(res1);
        Assertions.assertFalse(res2);
    }

    @Test
    void getProductsFromCart() {
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
        product2.setId(2L);

        //Act
        var res1 = cartsService.getProductsFromCart(owner);

        //Assert
        Assertions.assertEquals(1, res1.size());
        Assertions.assertEquals(product2, res1.get(0));
    }

    @Test
    void emptyOwnersCart() {
        //Arrange
        Account owner = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        );

        //Act
        cartsService.emptyOwnersCart(owner);
        var res = cartsService.findOwnersCartList(owner.getId());

        //Assert
        Assertions.assertEquals(1, res.size());
        Assertions.assertNull(res.get(0).getId());
        Assertions.assertNull(res.get(0).getOwner());
        Assertions.assertNull(res.get(0).getProduct());
    }
}