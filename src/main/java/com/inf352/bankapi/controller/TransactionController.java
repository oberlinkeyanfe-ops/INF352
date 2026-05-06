package com.inf352.bankapi.controller;

import com.inf352.bankapi.model.BankTransaction;
import com.inf352.bankapi.service.BankAccountService;
import com.inf352.bankapi.service.BankTransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "Gestion des dépôts, retraits et virements")
@SecurityRequirement(name = "bearerAuth")
public class TransactionController {

    private final BankTransactionService bankTransactionService;
    private final BankAccountService bankAccountService;

    public TransactionController(BankTransactionService bankTransactionService, BankAccountService bankAccountService) {
        this.bankTransactionService = bankTransactionService;
        this.bankAccountService = bankAccountService;
    }

    @PostMapping("/transfer")
    @Operation(summary = "Effectuer un virement entre comptes",
            description = "Transfère un montant d'un compte source à un compte de destination. Fonctionne entre différentes banques.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(name = "Virement",
                                    value = "{\"fromAccountNumber\":\"ACC-1A2B3C4D5E6F\",\"toAccountNumber\":\"ACC-6F5E4D3C2B1A\",\"amount\":150.75}"))
            ))
    public ResponseEntity<Void> transfer(@RequestBody Map<String, Object> request) {
        String fromAccountNumber = (String) request.get("fromAccountNumber");
        String toAccountNumber = (String) request.get("toAccountNumber");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());

        if (fromAccountNumber == null || toAccountNumber == null || amount == null) {
            throw new IllegalArgumentException("Les champs 'fromAccountNumber', 'toAccountNumber' et 'amount' sont obligatoires.");
        }

        bankTransactionService.transfer(fromAccountNumber, toAccountNumber, amount);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/withdraw-mobile")
    @Operation(summary = "Effectuer un retrait vers un compte mobile (Momo/OM)",
            description = "Simule un retrait d'un compte bancaire vers un numéro de téléphone mobile. La limite pour cette opération est de 500 000.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(name = "Retrait Mobile",
                                    value = "{\"fromAccountNumber\":\"ACC-1A2B3C4D5E6F\",\"mobileNumber\":\"+237699000000\",\"amount\":50000}"))
            ))
    public ResponseEntity<BankTransaction> withdrawToMobile(@RequestBody Map<String, Object> request) {
        String fromAccountNumber = (String) request.get("fromAccountNumber");
        String mobileNumber = (String) request.get("mobileNumber");
        BigDecimal amount = new BigDecimal(request.get("amount").toString());

        if (fromAccountNumber == null || mobileNumber == null || amount == null) {
            throw new IllegalArgumentException("Les champs 'fromAccountNumber', 'mobileNumber' et 'amount' sont obligatoires.");
        }

        BankTransaction transaction = bankTransactionService.withdrawToMobile(fromAccountNumber, mobileNumber, amount);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/deposit/{accountNumber}")
    @Operation(summary = "Déposer de l'argent",
            description = "Dépose un montant dans un compte bancaire.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(name = "Dépôt",
                                    value = "{\"amount\":500}"))
            ))
    public ResponseEntity<BankTransaction> deposit(
            @Parameter(description = "Numéro de compte", example = "ACC-1A2B3C4D5E6F")
            @PathVariable String accountNumber,
            @RequestBody Map<String, BigDecimal> request) {
        return ResponseEntity.ok(bankTransactionService.deposit(accountNumber, extractAmount(request)));
    }

    @PostMapping("/withdraw/{accountNumber}")
    @Operation(summary = "Retirer de l'argent",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(name = "Retrait",
                                    value = "{\"amount\":100}"))
            ))
    public ResponseEntity<BankTransaction> withdraw(
            @Parameter(description = "Numéro de compte", example = "ACC-1A2B3C4D5E6F")
            @PathVariable String accountNumber,
            @RequestBody Map<String, BigDecimal> request) {
        return ResponseEntity.ok(bankTransactionService.withdraw(accountNumber, extractAmount(request)));
    }

    @GetMapping("/{accountNumber}")
    @Operation(summary = "Lister les transactions d'un compte")
    public ResponseEntity<List<BankTransaction>> listTransactions(
            @Parameter(description = "Numéro de compte", example = "ACC-1A2B3C4D5E6F")
            @PathVariable String accountNumber) {
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
