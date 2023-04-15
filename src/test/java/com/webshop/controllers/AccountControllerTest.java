package com.webshop.controllers;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@Order(2)
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void unauthorizedAccount() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithUserDetails(value = "new.email@gmail.com", userDetailsServiceBeanName = "userDetailsService")
    @WithMockUser
    void authorizedAccount() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isOk())
                .andExpect(view().name("account"))
                .andExpect(model().attributeExists("account", "bought", "products"));
    }
}