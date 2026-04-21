package com.inf352.bankapi.service;

import java.math.BigDecimal;

import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.model.BankTransaction;
import com.inf352.bankapi.model.TransactionType;
import com.inf352.bankapi.repository.BankTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankTransactionService {

    private final BankAccountService bankAccountService;
    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionService(BankAccountService bankAccountService,
            BankTransactionRepository bankTransactionRepository) {
        this.bankAccountService = bankAccountService;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    public BankTransaction deposit(String accountNumber, BigDecimal amount) {
        BankAccount account = bankAccountService.getAccount(accountNumber);
        BigDecimal before = account.getBalance();
        account.setBalance(before.add(amount));
        BankTransaction transaction = createTransaction(account, TransactionType.DEPOSIT, amount, before,
                account.getBalance());
        return bankTransactionRepository.save(transaction);
    }

    public BankTransaction withdraw(String accountNumber, BigDecimal amount) {
        BankAccount account = bankAccountService.getAccount(accountNumber);
        BigDecimal before = account.getBalance();
        if (before.compareTo(amount) < 0) {
            throw new com.inf352.bankapi.exception.InsufficientFundsException("Solde insuffisant");
        }
        account.setBalance(before.subtract(amount));
        BankTransaction transaction = createTransaction(account, TransactionType.WITHDRAWAL, amount, before,
                account.getBalance());
        return bankTransactionRepository.save(transaction);
    }

    private BankTransaction createTransaction(BankAccount account, TransactionType type, BigDecimal amount,
            BigDecimal beforeBalance, BigDecimal afterBalance) {
        BankTransaction transaction = new BankTransaction();
        transaction.setAccount(account);
        transaction.setType(type);
        transaction.setAmount(amount);
        transaction.setBeforeBalance(beforeBalance);
        transaction.setAfterBalance(afterBalance);
        return transaction;
    }
}
