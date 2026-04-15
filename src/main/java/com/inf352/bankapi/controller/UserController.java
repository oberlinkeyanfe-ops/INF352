package com.inf352.bankapi.controller;

import java.util.List;

import com.inf352.bankapi.model.BankUser;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "Endpoints de gestion des utilisateurs bancaires")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping
    @Operation(summary = "Ajouter un utilisateur", description = "Cree un nouvel utilisateur bancaire")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Utilisateur cree avec succes"),
            @ApiResponse(responseCode = "400", description = "Donnees invalides")
    })
    public ResponseEntity<BankUser> createUser(@Valid @RequestBody BankUser user) {
        BankUser createdUser = userRepository.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @GetMapping
    @Operation(summary = "Lister les utilisateurs", description = "Retourne tous les utilisateurs enregistres")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Liste des utilisateurs retournees")
    })
    public ResponseEntity<List<BankUser>> listUsers() {
        return ResponseEntity.ok(userRepository.selectUsers());
    }
}