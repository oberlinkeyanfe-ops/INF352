package com.inf352.bankapi.service;

import com.inf352.bankapi.model.Bank;
import com.inf352.bankapi.repository.BankRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BankService {

    private final BankRepository bankRepository;

    public BankService(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    public List<Bank> getBanks() {
        return bankRepository.findAll();
    }
}