package com.inf352.bankapi.controller;

import java.util.List;

import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@Tag(name = "Accounts", description = "Gestion des comptes bancaires")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final BankAccountService bankAccountService;

    public AccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/users/{userId}/accounts")
    @Operation(summary = "Creer un compte", description = "Cree un nouveau compte pour un utilisateur")
    public ResponseEntity<BankAccount> createAccount(@PathVariable Long userId) {
        return ResponseEntity.ok(bankAccountService.createAccount(userId));
    }

    @GetMapping("/users/{userId}/accounts")
    @Operation(summary = "Lister les comptes d'un utilisateur")
    public ResponseEntity<List<BankAccount>> listAccountsForUser(@PathVariable Long userId) {
        return ResponseEntity.ok(bankAccountService.listAccountsForUser(userId));
    }

    @GetMapping("/accounts/{accountNumber}")
    @Operation(summary = "Consulter un compte")
    public ResponseEntity<BankAccount> getAccount(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankAccountService.getAccount(accountNumber));
    }
}
