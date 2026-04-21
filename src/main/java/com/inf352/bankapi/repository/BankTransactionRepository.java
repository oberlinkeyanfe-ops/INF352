package com.inf352.bankapi.repository;

import java.util.List;

import com.inf352.bankapi.model.BankTransaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

    List<BankTransaction> findByAccountAccountNumberOrderByCreatedAtDesc(String accountNumber);
}
