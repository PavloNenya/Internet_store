package com.webshop.services;

import com.webshop.dto.AccountDTO;
import com.webshop.exceptions.IncorrectEmailException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
class AuthenticationServiceTest {
    @Autowired
    private AuthenticationService authenticationService;

    @Test
    void isEmailValid() {
        // Arrange
        String validEmail1 = "email.mail@gmail.com";
        String validEmail2 = "email.mail@gmail.com";
        String invalidEmail1 = "emailgmail.com";
        String invalidEmail2 = "emai@gmailcom";

        //Act
        var res1 = authenticationService.isEmailValid(validEmail1);
        var res2 = authenticationService.isEmailValid(validEmail2);
        var res3 = authenticationService.isEmailValid(invalidEmail1);
        var res4 = authenticationService.isEmailValid(invalidEmail2);

        //Assert
        Assertions.assertTrue(res1);
        Assertions.assertTrue(res2);
        assertFalse(res3);
        assertFalse(res4);
    }

    @Test
    void validation() {
        //Arrange
        var accountDTO = new AccountDTO(
                "Pavlo",
                "pavlo@nenya",
                "password123"
        );

        //Act + Assert
        Assertions.assertThrows(IncorrectEmailException.class, () -> authenticationService.validation(accountDTO));
    }
}