package com.inf352.bankapi.controller;

import java.util.List;

import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.service.BankUserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints de gestion des utilisateurs bancaires")
public class UserController {

    private final BankUserService bankUserService;

    public UserController(BankUserService bankUserService) {
        this.bankUserService = bankUserService;
    }

    @PostMapping
    @Operation(summary = "Ajouter un utilisateur", description = "Cree un nouvel utilisateur bancaire")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur cree avec succes"),
            @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    public ResponseEntity<BankUser> createUser(@Valid @RequestBody BankUser user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bankUserService.createUser(user));
    }

    @GetMapping
    @Operation(summary = "Lister les utilisateurs", description = "Retourne tous les utilisateurs enregistres")
    @SecurityRequirement(name = "bearerAuth")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs retournees")
    })
    public ResponseEntity<List<BankUser>> listUsers() {
        return ResponseEntity.ok(bankUserService.listUsers());
    }

    @GetMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Consulter un utilisateur")
    public ResponseEntity<BankUser> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(bankUserService.getUser(id));
    }

    @PutMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Mettre a jour un utilisateur")
    public ResponseEntity<BankUser> updateUser(@PathVariable Long id, @Valid @RequestBody BankUser user) {
        return ResponseEntity.ok(bankUserService.updateUser(id, user));
    }

    @DeleteMapping("/{id}")
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "Supprimer un utilisateur")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        bankUserService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}