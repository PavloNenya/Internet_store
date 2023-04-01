package com.webshop.services;

import com.webshop.exceptions.AccountAlreadyExistException;
import com.webshop.exceptions.IncorrectEmailException;
import com.webshop.models.Account;
import com.webshop.dto.AccountDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AccountsService accountsService;

//    public Optional<Account> login(AccountDTO accountDTO) {
//        return accountsService.getAllAccounts().stream()
//                .filter(
//                        a -> a.getEmail().equals(accountDTO.getEmail())
//                        && a.getPassword().equals(accountDTO.getPassword())
//                )
//                .findFirst();
//    }

    public boolean isEmailValid(String email) {
        return Pattern.compile("[\\w.]+@[\\w.]+\\.[\\w.]+")
                .matcher(email)
                .matches();
    }

    public void validation(AccountDTO accountDTO) throws AccountAlreadyExistException, IncorrectEmailException {
        if(accountsService.existsByEmail(accountDTO.getEmail())) {
            throw new AccountAlreadyExistException("Account exists!");
        } else if (!isEmailValid(accountDTO.getEmail())) {
            throw new IncorrectEmailException("Incorrect email!");
        }
    }
}
