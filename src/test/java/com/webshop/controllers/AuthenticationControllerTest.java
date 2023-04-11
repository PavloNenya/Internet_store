package com.webshop.controllers;

import com.webshop.dto.AccountDTO;
import com.webshop.exceptions.AccountAlreadyExistException;
import com.webshop.models.Account;
import com.webshop.repositories.AccountsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private PasswordEncoder passwordEncoder;
    @MockBean
    AccountsRepository accountsRepository;

    @BeforeEach
    void init() {
        Account account1 = new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya123@gmail.com",
                null
        );
        Account account2 = new Account(
                321L,
                "Ivan",
                "qwerty123",
                "ivan.email@gmail.com",
                null
        );
        List<Account> accounts = new ArrayList<>();
        accounts.add(account1);
        accounts.add(account2);
        when(passwordEncoder.encode(any(String.class))).thenAnswer(i -> i.getArguments()[0]);
        when(accountsRepository.save(any(Account.class))).thenAnswer(invocation -> {
            var newAccount = (Account) invocation.getArguments()[0];
            accounts.add(newAccount);
            return newAccount;
        });
        when(accountsRepository.findAccountByEmail("pavel.nenya123@gmail.com")).thenReturn(Optional.of(account1));
        when(accountsRepository.findAccountByEmail("ivan.email@gmail.com")).thenReturn(Optional.of(account2));
        when(accountsRepository.findAll()).thenReturn(accounts);
    }

    @Test
    void getRegisterTest() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("accountDTO"))
                .andExpect(view().name("register"));
    }

    @Test
    void postRegisterTestShouldBeBadRequest() throws Exception {
        var accountDTO = new AccountDTO(
                "Pavlo",
                "pavel.nenya123@gmail.com",
                "123"
        );
        mockMvc.perform(post("/register").flashAttr("accountDTO", accountDTO))
                .andExpect(status().isBadRequest())
                .andExpect(model().attributeExists("accountDTO"))
                .andExpect(view().name("register"))
                .andExpect(content().string(containsString("Account already exists!")));
    }

    @Test
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
        Assertions.assertEquals(3, accountsRepository.findAll().size());
    }

    @Test
    @WithMockUser(username = "pavel.nenya123@gmail.com", password = "qwerty123")
//    @WithUserDetails()
    void loginPost() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
//
//        mockMvc.perform(post("/login") //?username=pavel.nenya123@gmail.com&password=123
//                        .param("username", "pavel.nenya123@gmail.com")
//                        .param("password", "qwerty123"))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/"));

        mockMvc.perform(get("/cart"))
                .andExpect(status().isOk());

//                .andExpect(view().name("index"));

//        mockMvc.perform(
//                formLogin("/login")
//                        .user("pavel.nenya228@gmail.com")
//                        .password("123"))
//                .andExpect(status().isFound())
//                .andExpect(redirectedUrl("/"));

    }
}