package com.webshop.services;

import com.webshop.dto.AccountDTO;
import com.webshop.exceptions.AccountAlreadyExistException;
import com.webshop.models.Account;
import com.webshop.repositories.AccountsRepository;
import org.apache.catalina.realm.GenericPrincipal;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
class AccountsServiceTest {
    @MockBean
    private AccountsRepository accountsRepository;
    @Autowired
    private AccountsService accountsService;
    @MockBean
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void init() {
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
        when(passwordEncoder.encode(any())).thenAnswer(i -> i.getArguments()[0]);
        when(accountsRepository.save(any(Account.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        when(accountsRepository.findAccountByEmail("pavel.nenya228@gmail.com")).thenReturn(Optional.of(account1));
        when(accountsRepository.findAccountByEmail("pavel.nenya228@gmail.com")).thenReturn(Optional.of(account1));
        when(accountsRepository.findAccountByEmail("ivan.email@gmail.com")).thenReturn(Optional.of(account2));
        when(accountsRepository.findAll()).thenReturn(List.of(account1, account2));
    }

    @Test
    void existsByEmail() {
        // Arrange
        String email1 = "pavel.nenya228@gmail.com";
        String email2 = "notexisting@gmail.com";

        // Act
        boolean foundAccount2 = accountsService.existsByEmail(email2);

        // Assert
        Assertions.assertThrows(AccountAlreadyExistException.class, () -> accountsService.existsByEmail(email1));
        Assertions.assertFalse(foundAccount2);
    }

    @Test
    void saveTest() {
        // Arrange
        Account account = new Account();
        account.setEmail("some@gmail.com");

        // Act
        Account createdAccount = accountsService.save(account);

        // Assert
        Assertions.assertEquals(createdAccount, account);
        Mockito.verify(accountsRepository, Mockito.times(1)).save(account);
    }

    @Test
    void getByEmail() {
        // Arrange
        String email1 = "pavel.nenya228@gmail.com";
        String email2 = "notexisting@gmail.com";

        // Act
        Optional<Account> foundAccount1 = accountsService.getByEmail(email1);
        Optional<Account> foundAccount2 = accountsService.getByEmail(email2);

        // Assert
        Assertions.assertEquals("Pavlo", foundAccount1.get().getName());
        Assertions.assertFalse(foundAccount2.isPresent());
    }

    @Test
    void getAllAccounts() {
        // Arrange
        List<Account> list = List.of(
                new Account(
                    123L,
                    "Pavlo",
                    "qwerty123",
                    "pavel.nenya228@gmail.com",
                    null
                ),
                new Account(
                    321L,
                    "Ivan",
                    "qwerty123",
                    "ivan.email@gmail.com",
                    null
                )
        );

        // Act
        List<Account> list1 = accountsService.getAllAccounts();

        //Assert

        Assertions.assertEquals(list.get(0), list1.get(0));
        Assertions.assertEquals(list.get(1), list1.get(1));
    }

    @Test
    void dtoSaveTest() {
        //Arrange
        AccountDTO accountDTO = new AccountDTO("Egor", "ehor@gmail.com", "qwerty123");

        //Act
        Account account = accountsService.save(accountDTO);
        account.setId(123L);
        //Assert
        Assertions.assertEquals(new Account(
                123L,
                "Egor",
                "qwerty123",
                "ehor@gmail.com",
                null
        ), account);
    }

    @Test
    void findAccountByPrinciple() {
        Principal principal = new GenericPrincipal("pavel.nenya228@gmail.com");
        //Act
        var result = accountsService.findAccountByPrincipal(null);
        var result2 = accountsService.findAccountByPrincipal(principal);
        //Assert
        Assertions.assertNull(result);
        Assertions.assertEquals(new Account(
                123L,
                "Pavlo",
                "qwerty123",
                "pavel.nenya228@gmail.com",
                null
        ), result2);
    }
}