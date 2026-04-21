package com.inf352.bankapi.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.inf352.bankapi.model.BankTransaction;
import com.inf352.bankapi.service.BankAccountService;
import com.inf352.bankapi.service.BankTransactionService;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Transactions", description = "Depot et retrait d'argent")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final BankTransactionService bankTransactionService;
    private final BankAccountService bankAccountService;

    public TransactionController(BankTransactionService bankTransactionService, BankAccountService bankAccountService) {
        this.bankTransactionService = bankTransactionService;
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/{accountNumber}/deposit")
    @Operation(summary = "Deposer de l'argent")
    public ResponseEntity<BankTransaction> deposit(@PathVariable String accountNumber,
            @RequestBody Map<String, BigDecimal> request) {
        return ResponseEntity.ok(bankTransactionService.deposit(accountNumber, extractAmount(request)));
    }

    @PostMapping("/{accountNumber}/withdraw")
    @Operation(summary = "Retirer de l'argent")
    public ResponseEntity<BankTransaction> withdraw(@PathVariable String accountNumber,
            @RequestBody Map<String, BigDecimal> request) {
        return ResponseEntity.ok(bankTransactionService.withdraw(accountNumber, extractAmount(request)));
    }

    @GetMapping("/{accountNumber}/transactions")
    @Operation(summary = "Lister les transactions d'un compte")
    public ResponseEntity<List<BankTransaction>> listTransactions(@PathVariable String accountNumber) {
        return ResponseEntity.ok(bankAccountService.listTransactions(accountNumber));
    }

    private BigDecimal extractAmount(Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("Le montant doit etre strictement positif");
        }
        return amount;
    }
}
