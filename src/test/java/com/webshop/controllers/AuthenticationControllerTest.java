package com.webshop.controllers;

import com.webshop.dto.AccountDTO;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Order(1)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void getRegisterTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("accountDTO"))
                .andExpect(view().name("register"));
    }

    @Test
    @Order(2)
    void postRegisterTestShouldBeBadRequest() throws Exception {
        var accountDTO = new AccountDTO(
                "Pavlo",
                "new.email@gmail.com",
                "123"
        );
        mockMvc.perform(post("/register")
                        .flashAttr("accountDTO", accountDTO))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("accountDTO"))
                .andExpect(view().name("register"))
                .andExpect(content().string(containsString("Account already exists!")));
    }

    @Test
    @Order(1)
    void postRegisterTestShouldBeOk() throws Exception {
        var accountDTO = new AccountDTO(
                "Ihor",
                "new.email@gmail.com",
                "123"
        );
        mockMvc.perform(post("/register").flashAttr("accountDTO", accountDTO))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("accountDTO"))
                .andExpect(view().name("success"))
                .andExpect(content().string(containsString("Account registered successfully")));

    }

    @Test
    @Order(3)
    void loginPostShouldBeOk() throws Exception {
        mockMvc.perform(
                formLogin("/login")
                        .user("new.email@gmail.com")
                        .password("123"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/"));
    }

    @Test
    @Order(4)
    void loginPostShouldBeFound() throws Exception {
        mockMvc.perform(
                        post("/login")
                                .param("user","wrong.email@gmail.com")
                                .param("password", "321"))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/login?error"));

        mockMvc.perform(
                        get("/login?error"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Invalid username or password.")));
    }

    @Test
    void loginGet() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }
}