package com.inf352.bankapi.service;

import java.math.BigDecimal;
import java.util.List;

import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.repository.BankAccountRepository;
import com.inf352.bankapi.repository.BankTransactionRepository;
import com.inf352.bankapi.controller.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;
    private final UserRepository userRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository,
            BankTransactionRepository bankTransactionRepository,
            UserRepository userRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankTransactionRepository = bankTransactionRepository;
        this.userRepository = userRepository;
    }

    public BankAccount createAccount(Long userId) {
        BankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));

        BankAccount account = new BankAccount();
        account.setUser(user);
        return bankAccountRepository.save(account);
    }

    public BankAccount getAccount(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable"));
    }

    public List<BankAccount> listAccountsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur introuvable");
        }
        return bankAccountRepository.findByUserId(userId);
    }

    public BankAccount deposit(String accountNumber, BigDecimal amount) {
        BankAccount account = getAccount(accountNumber);
        account.setBalance(account.getBalance().add(amount));
        return bankAccountRepository.save(account);
    }

    public BankAccount withdraw(String accountNumber, BigDecimal amount) {
        BankAccount account = getAccount(accountNumber);
        if (account.getBalance().compareTo(amount) < 0) {
            throw new com.inf352.bankapi.exception.InsufficientFundsException("Solde insuffisant");
        }
        account.setBalance(account.getBalance().subtract(amount));
        return bankAccountRepository.save(account);
    }

    public List<com.inf352.bankapi.model.BankTransaction> listTransactions(String accountNumber) {
        return bankTransactionRepository.findByAccountAccountNumberOrderByCreatedAtDesc(accountNumber);
    }
}
