package com.inf352.bankapi.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.model.AccountType;
import com.inf352.bankapi.model.Bank;
import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.repository.AccountTypeRepository;
import com.inf352.bankapi.repository.BankAccountRepository;
import com.inf352.bankapi.repository.BankRepository;
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
    private final BankRepository bankRepository;
    private final AccountTypeRepository accountTypeRepository;

    public BankAccountService(BankAccountRepository bankAccountRepository,
                              BankTransactionRepository bankTransactionRepository,
                              UserRepository userRepository,
                              BankRepository bankRepository,
                              AccountTypeRepository accountTypeRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankTransactionRepository = bankTransactionRepository;
        this.userRepository = userRepository;
        this.bankRepository = bankRepository;
        this.accountTypeRepository = accountTypeRepository;
    }

    public BankAccount createAccount(Long userId, Long bankId, Long accountTypeId) {
        BankUser user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable"));
        Bank bank = bankRepository.findById(bankId)
                .orElseThrow(() -> new ResourceNotFoundException("Banque introuvable"));
        AccountType accountType = accountTypeRepository.findById(accountTypeId)
                .orElseThrow(() -> new ResourceNotFoundException("Type de compte introuvable"));

        BankAccount account = new BankAccount();
        account.setUser(user);
        account.setBank(bank);
        account.setAccountType(accountType);
        return bankAccountRepository.save(account);
    }

    public Optional<BankAccount> getAccount(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber);
    }

    public List<BankAccount> listAccountsForUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("Utilisateur introuvable");
        }
        return bankAccountRepository.findByUserId(userId);
    }

    public BankAccount deposit(String accountNumber, BigDecimal amount) {
        BankAccount account = getAccount(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable"));
        account.setBalance(account.getBalance().add(amount));
        return bankAccountRepository.save(account);
    }

    public BankAccount withdraw(String accountNumber, BigDecimal amount) {
        BankAccount account = getAccount(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte introuvable"));
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
