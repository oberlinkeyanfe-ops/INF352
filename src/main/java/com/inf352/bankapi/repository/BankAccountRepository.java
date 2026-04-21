package com.inf352.bankapi.repository;

import java.util.List;
import java.util.Optional;

import com.inf352.bankapi.model.BankAccount;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankAccountRepository extends JpaRepository<BankAccount, Long> {

    Optional<BankAccount> findByAccountNumber(String accountNumber);

    List<BankAccount> findByUserId(Long userId);
}
