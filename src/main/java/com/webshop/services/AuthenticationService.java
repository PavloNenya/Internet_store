package com.webshop.services;

import com.webshop.dto.AccountDTO;
import com.webshop.exceptions.IncorrectEmailException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    public boolean isEmailValid(String email) {
        return Pattern.compile("[\\w.]+@[\\w.]+\\.[\\w.]+")
                .matcher(email)
                .matches();
    }

    public void validation(AccountDTO accountDTO) throws IncorrectEmailException {
        if (!isEmailValid(accountDTO.getEmail())) {
            throw new IncorrectEmailException("Incorrect email!");
        }
    }
}
