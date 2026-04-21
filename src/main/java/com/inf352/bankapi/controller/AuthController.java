package com.inf352.bankapi.controller;

import java.util.Map;

import com.inf352.bankapi.service.BankUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Gestion des tokens d'acces")
@Validated
public class AuthController {

    private final BankUserService bankUserService;

    public AuthController(BankUserService bankUserService) {
        this.bankUserService = bankUserService;
    }

    @PostMapping("/token")
    @Operation(summary = "Obtenir un token", description = "Retourne le token API associe a un utilisateur")
    public ResponseEntity<Map<String, String>> issueToken(@RequestBody @NotNull Map<String, String> request) {
        String email = request.get("email");
        String phone = request.get("phone");

        if (email == null || email.isBlank() || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Les champs email et phone sont obligatoires");
        }

        String token = bankUserService.issueToken(email, phone);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
