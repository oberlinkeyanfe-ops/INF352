package com.inf352.bankapi.controller;

import com.inf352.bankapi.model.AccountType;
import com.inf352.bankapi.service.AccountTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/account-types")
@Tag(name = "Account Types", description = "Account type management")
public class AccountTypeController {

    private final AccountTypeService accountTypeService;

    public AccountTypeController(AccountTypeService accountTypeService) {
        this.accountTypeService = accountTypeService;
    }

    @GetMapping
    public List<AccountType> getAccountTypes() {
        return accountTypeService.getAccountTypes();
    }
}