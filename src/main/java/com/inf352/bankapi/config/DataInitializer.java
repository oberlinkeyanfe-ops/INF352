package com.inf352.bankapi.config;

import com.inf352.bankapi.model.AccountType;
import com.inf352.bankapi.model.Bank;
import com.inf352.bankapi.repository.AccountTypeRepository;
import com.inf352.bankapi.repository.BankRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

//@Component
public class DataInitializer implements CommandLineRunner {

    private final BankRepository bankRepository;
    private final AccountTypeRepository accountTypeRepository;

    public DataInitializer(BankRepository bankRepository, AccountTypeRepository accountTypeRepository) {
        this.bankRepository = bankRepository;
        this.accountTypeRepository = accountTypeRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (bankRepository.count() == 0) {
            bankRepository.saveAll(List.of(
                    new Bank("CCA", 0.03, 1000000.0),
                    new Bank("Afriland First Bank", 0.035, 1500000.0),
                    new Bank("UBA", 0.025, 2000000.0),
                    new Bank("Société Générale", 0.04, 2500000.0),
                    new Bank("BICEC", 0.032, 1200000.0),
                    new Bank("Ecobank", 0.038, 1800000.0),
                    new Bank("Standard Chartered Bank", 0.045, 3000000.0),
                    new Bank("BGFI Bank", 0.036, 1600000.0),
                    new Bank("NFC Bank", 0.028, 900000.0),
                    new Bank("CBC", 0.031, 1100000.0)
            ));
        }

        if (accountTypeRepository.count() == 0) {
            accountTypeRepository.saveAll(List.of(
                    new AccountType("Courant", "Compte pour les transactions quotidiennes"),
                    new AccountType("Épargne", "Compte pour épargner de l'argent"),
                    new AccountType("Entreprise", "Compte pour les entreprises")
            ));
        }
    }
}