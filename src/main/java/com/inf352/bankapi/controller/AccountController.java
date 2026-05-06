package com.inf352.bankapi.controller;

import java.util.List;
import java.util.Map;

import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.service.BankAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "Gestion des comptes bancaires")
@SecurityRequirement(name = "bearerAuth")
public class AccountController {

    private final BankAccountService bankAccountService;

    public AccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping
    @Operation(summary = "Creer un compte", description = "Cree un nouveau compte pour un utilisateur")
    public ResponseEntity<BankAccount> createAccount(@RequestBody Map<String, Object> requestBody) {
        Long userId = ((Number) requestBody.get("userId")).longValue();
        Long bankId = ((Number) requestBody.get("bankId")).longValue();
        Long accountTypeId = ((Number) requestBody.get("accountTypeId")).longValue();
        return ResponseEntity.ok(bankAccountService.createAccount(userId, bankId, accountTypeId));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Lister les comptes d'un utilisateur")
    public ResponseEntity<List<BankAccount>> listAccountsForUser(
            @Parameter(description = "Identifiant utilisateur", example = "1")
            @PathVariable Long userId) {
        return ResponseEntity.ok(bankAccountService.listAccountsForUser(userId));
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Consulter un compte")
    public ResponseEntity<BankAccount> getAccount(
            @Parameter(description = "Numero de compte", example = "ACC-1A2B3C4D5E6F")
            @PathVariable String accountNumber) {
        return ResponseEntity.ok(bankAccountService.getAccount(accountNumber)
                .orElseThrow(() -> new com.inf352.bankapi.exception.ResourceNotFoundException("Compte introuvable")));
    }
}
