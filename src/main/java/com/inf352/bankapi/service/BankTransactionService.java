package com.inf352.bankapi.service;

import java.math.BigDecimal;

import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.exception.InsufficientFundsException;
import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.model.BankTransaction;
import com.inf352.bankapi.model.TransactionType;
import com.inf352.bankapi.repository.BankAccountRepository;
import com.inf352.bankapi.repository.BankTransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BankTransactionService {

    private final BankAccountRepository bankAccountRepository;
    private final BankTransactionRepository bankTransactionRepository;

    public BankTransactionService(BankAccountRepository bankAccountRepository,
            BankTransactionRepository bankTransactionRepository) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankTransactionRepository = bankTransactionRepository;
    }

    public BankTransaction deposit(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du dépôt doit être positif.");
        }
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte destinataire non trouvé."));

        toAccount.setBalance(toAccount.getBalance().add(amount));
        bankAccountRepository.save(toAccount);

        BankTransaction transaction = new BankTransaction();
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(amount);
        transaction.setAccount(toAccount);
        return bankTransactionRepository.save(transaction);
    }

    public BankTransaction withdraw(String accountNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif.");
        }
        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte source non trouvé."));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fonds insuffisants pour le retrait.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        bankAccountRepository.save(fromAccount);

        BankTransaction transaction = new BankTransaction();
        transaction.setType(TransactionType.WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setAccount(fromAccount);
        return bankTransactionRepository.save(transaction);
    }

    public void transfer(String fromAccountNumber, String toAccountNumber, BigDecimal amount) {
        if (fromAccountNumber.equals(toAccountNumber)) {
            throw new IllegalArgumentException("Le compte source et le compte destination ne peuvent pas être identiques.");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du transfert doit être positif.");
        }

        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte source non trouvé."));
        BankAccount toAccount = bankAccountRepository.findByAccountNumber(toAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte destinataire non trouvé."));

        if (!fromAccount.getUser().equals(toAccount.getUser())) {
            throw new IllegalArgumentException("Les transferts ne sont autorisés qu'entre les comptes du même utilisateur.");
        }

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fonds insuffisants pour le transfert.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        bankAccountRepository.save(fromAccount);
        bankAccountRepository.save(toAccount);

        BankTransaction transaction = new BankTransaction();
        transaction.setType(TransactionType.TRANSFER_DEBIT);
        transaction.setAmount(amount);
        transaction.setAccount(fromAccount);
        bankTransactionRepository.save(transaction);
    }

    public BankTransaction withdrawToMobile(String fromAccountNumber, String mobileNumber, BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant du retrait doit être positif.");
        }
        if (amount.compareTo(new BigDecimal(500000)) > 0) {
            throw new IllegalArgumentException("Le montant du retrait mobile ne peut pas dépasser 500 000.");
        }
        BankAccount fromAccount = bankAccountRepository.findByAccountNumber(fromAccountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Compte source non trouvé."));

        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientFundsException("Fonds insuffisants pour le retrait mobile.");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        bankAccountRepository.save(fromAccount);

        BankTransaction transaction = new BankTransaction();
        transaction.setType(TransactionType.MOBILE_WITHDRAWAL);
        transaction.setAmount(amount);
        transaction.setAccount(fromAccount);
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
