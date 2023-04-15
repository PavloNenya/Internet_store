package com.webshop.controllers;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(4)
class CartControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(3)
    void cart() throws Exception {
        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Description")))
                .andExpect(content().string(containsString("Price")));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(2)
    void addToCartPost() throws Exception {
        mockMvc.perform(post("/cart/add/1"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/products/1"));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(1)
    void addToCartGet() throws Exception {


        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("cart"))
                .andExpect(content().string(containsString("Title")))
                .andExpect(content().string(containsString("Description")))
                .andExpect(content().string(containsString("Price")));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(4)
    void deleteProductFromCart() throws Exception {
        mockMvc.perform(post("/cart/delete/1"))
                .andExpect(view().name("cart"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/cart"));
        mockMvc.perform(get("/cart"))
                .andExpect(content().string(not(containsString("asdfgh"))))
                .andExpect(content().string(not(containsString("qwertyu"))));
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    @Order(5)
    void buyAllPost() throws Exception {
        mockMvc.perform(post("/cart/buy"))
                .andExpect(view().name("success"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/cart"))
                .andExpect(content().string(not(containsString("/products/"))));
    }
}