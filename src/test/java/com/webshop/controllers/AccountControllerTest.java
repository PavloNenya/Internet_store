package com.webshop.controllers;

import com.webshop.repositories.AccountsRepository;
import com.webshop.services.AccountsService;
import com.webshop.services.OrdersService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.Mapping;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
    @Autowired
    private MockMvc mockMvc;
//

    @Test
    void unauthorizedAccount() throws Exception {
        mockMvc.perform(get("/account"))
                .andExpect(status().isUnauthorized())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    void authorizedAccount() throws Exception {
        mockMvc.perform(post("/login?username=pavel.nenya123@gmail.com&password=qwerty123")) //?username=pavel.nenya228@gmail.com&password=123
//                        .param("username", "pavel.nenya228@gmail.com")
//                        .param("password", "123"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
//        mockMvc.perform(get("/account"))
//                .andExpect(status().isUnauthorized());
//                .andExpect(view().name("account"))
//                .andExpect(model().attributeExists("account", "bought", "products"));
    }
}