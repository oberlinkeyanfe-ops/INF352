package com.inf352.bankapi.controller;

import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.service.BankUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Auth", description = "Gestion des tokens d'acces et de la vérification")
@Validated
public class AuthController {

    private final BankUserService bankUserService;

    public AuthController(BankUserService bankUserService) {
        this.bankUserService = bankUserService;
    }

    @PostMapping("/verify")
    @Operation(summary = "Vérifier l'email d'un utilisateur", description = "Valide le code de vérification envoyé par email pour activer un compte.")
    public ResponseEntity<Map<String, String>> verifyEmail(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        String code = request.get("code");
        BankUser user = bankUserService.verifyUser(email, code);
        return ResponseEntity.ok(Map.of("message", "Utilisateur vérifié avec succès. Vous pouvez maintenant obtenir un token.", "userId", user.getId().toString()));
    }

    @PostMapping("/token")
    @Operation(summary = "Obtenir un token", description = "Retourne le token API associe a un utilisateur. L'utilisateur doit être vérifié.")
    public ResponseEntity<Map<String, String>> issueToken(
            @RequestBody
            @NotNull
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = Map.class),
                            examples = @ExampleObject(name = "Auth",
                                    value = "{\"email\":\"awa@example.com\",\"phone\":\"+237699000000\"}")))
            Map<String, String> request) {
        String email = request.get("email");
        String phone = request.get("phone");

        if (email == null || email.isBlank() || phone == null || phone.isBlank()) {
            throw new IllegalArgumentException("Les champs email et phone sont obligatoires");
        }

        String token = bankUserService.issueToken(email, phone);
        return ResponseEntity.ok(Map.of("token", token));
    }
}
