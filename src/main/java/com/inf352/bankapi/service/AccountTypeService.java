package com.inf352.bankapi.service;
import com.inf352.bankapi.model.AccountType;
import com.inf352.bankapi.repository.AccountTypeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountTypeService {

    private final AccountTypeRepository accountTypeRepository;

    public AccountTypeService(AccountTypeRepository accountTypeRepository) {
        this.accountTypeRepository = accountTypeRepository;
    }

    public List<AccountType> getAccountTypes() {
        return accountTypeRepository.findAll();
    }
}