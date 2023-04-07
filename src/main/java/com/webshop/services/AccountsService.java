package com.webshop.services;

import com.webshop.dto.AccountDTO;
import com.webshop.exceptions.AccountAlreadyExistException;
import com.webshop.models.Account;
import com.webshop.models.Product;
import com.webshop.repositories.AccountsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) throws AccountAlreadyExistException {
        if(getByEmail(email).isPresent()) {
            throw new AccountAlreadyExistException("Account already exists!");
        } else return false;
    }
    public Account save(AccountDTO accountDTO) {
        Account account = Account.builder()
                .name(accountDTO.getName())
                .email(accountDTO.getEmail())
                .password(passwordEncoder.encode(accountDTO.getPassword()))
                .build();
        return accountsRepository.save(account);
    }
    public Account save(Account account) {
        return accountsRepository.save(account);
    }
    public Optional<Account> getByEmail(String email) {
        return accountsRepository.findAll().stream()
                .filter(account -> account.getEmail().equals(email))
                .findFirst();
    }
    public List<Account> getAllAccounts() {
        return accountsRepository.findAll().stream()
                .toList();
    }
    public Account findAccountByPrincipal(Principal principal) {
        if (principal == null) return null;
        return accountsRepository.findAccountByEmail(principal.getName())
                .orElse(null);
    }
}
