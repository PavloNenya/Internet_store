package com.webshop.controllers;

import com.webshop.exceptions.ProductNotFoundException;
import com.webshop.models.Account;
import com.webshop.models.Product;
import com.webshop.repositories.ProductsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductsRepository productsRepository;

    @BeforeEach
    void init() {
        var seller1 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        var seller2 = new Account(
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
                        seller1
                ),
                new Product(
                        "Title2",
                        "Description2",
                        35.90,
                        seller2
                )
        );
        products.get(0).setId(1L);
        products.get(0).setActive(false);
        products.get(1).setId(2L);
        seller1.setProducts(List.of(products.get(0)));
        seller2.setProducts(List.of(products.get(1)));
        when(productsRepository.findProductsBySeller(any(Account.class)))
                .thenAnswer((i) -> products
                        .stream()
                        .filter(product -> product.getSeller().getId().equals(((Account) i.getArguments()[0]).getId()))
                        .toList());
        when(productsRepository.findAll()).thenAnswer(i -> {
            products.get(0).setActive(true);
            products.get(1).setActive(true);
            return products;
        });
        when(productsRepository.save(any(Product.class))).thenAnswer(i -> i.getArguments()[0]);
        when(productsRepository.findById(1L)).thenAnswer(invocation -> Optional.of(products.get(0)));
        when(productsRepository.findById(2L)).thenAnswer(invocation -> Optional.of(products.get(1)));
        doAnswer(invocation -> {
            Product product = productsRepository.findById((Long)invocation.getArguments()[0])
                    .orElseThrow(ProductNotFoundException::new);
            product.setActive(invocation.getArgument(1, Boolean.class));
            return product;
        }).when(productsRepository).updateProductActivity(any(Long.class), any(Boolean.class));

    }

    @Test
    void getAddProduct() throws Exception {

    }

    @Test
    void postAddProduct() {

    }

    @Test
    @WithMockUser
    void productPage() throws Exception {
        mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Price")))
                .andExpect(content().string(containsString("25.9")));
//                .andExpect(content().string(containsString("35.9")));
    }

    @Test
    void getAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Price")))
                .andExpect(content().string(containsString("25.9")))
                .andExpect(content().string(containsString("35.9")));
    }

    @Test
    void searchPage() throws Exception {
        mockMvc.perform(get("/search")
                        .param("search", "Title"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Price")))
                .andExpect(content().string(containsString("25.9")))
                .andExpect(content().string(containsString("35.9")));

        mockMvc.perform(get("/search")
                        .param("search", "2"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Price")))
                .andExpect(content().string(not(containsString("25.9"))))
                .andExpect(content().string(containsString("35.9")));
    }

    @Test
    void productDeleteGet() {

    }

    @Test
    void productDelete() {

    }

    @Test
    void handleException() {

    }
}