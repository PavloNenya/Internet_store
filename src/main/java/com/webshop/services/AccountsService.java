package com.webshop.services;

import com.webshop.models.Account;
import com.webshop.dto.AccountDTO;
import com.webshop.repositories.AccountsRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class AccountsService {
    private final AccountsRepository accountsRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean existsByEmail(String email) {
        return getByEmail(email).isPresent();
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
        return StreamSupport
                .stream(accountsRepository.findAll().spliterator(), false)
                .filter(account -> account.getEmail().equals(email))
                .findFirst();
    }

    public List<Account> getAllAccounts() {
        return StreamSupport.stream(accountsRepository.findAll().spliterator(), false)
                .toList();
    }

    public Account findAccountByPrincipal(Principal principal) {
        if (principal == null) return null;
        return accountsRepository.findAccountByEmail(principal.getName())
                .orElse(null);
    }
}
