package com.webshop.repositories;

import com.webshop.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountsRepository extends JpaRepository<Account, Long> {
    Optional<Account> findAccountByEmail(String email);
}
