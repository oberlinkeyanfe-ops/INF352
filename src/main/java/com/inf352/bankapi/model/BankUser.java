package com.inf352.bankapi.model;

import java.time.Instant;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "bank_users")
@io.swagger.v3.oas.annotations.media.Schema(description = "Utilisateur bancaire manipulé par l'API")
public class BankUser {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Schema(description = "Identifiant interne", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        private Long id;

        @NotBlank(message = "Le prenom est obligatoire")
        @Size(max = 80, message = "Le prenom ne doit pas depasser 80 caracteres")
        @Column(name = "first_name", nullable = false)
        @Schema(description = "Prenom", example = "Awa")
        private String firstName;

        @NotBlank(message = "Le nom est obligatoire")
        @Size(max = 80, message = "Le nom ne doit pas depasser 80 caracteres")
        @Column(name = "last_name", nullable = false)
        @Schema(description = "Nom", example = "Diallo")
        private String lastName;

        @NotBlank(message = "L'email est obligatoire")
        @Email(message = "L'email doit etre valide")
        @Column(nullable = false)
        @Schema(description = "Adresse email", example = "awa@example.com")
        private String email;

        @NotBlank(message = "Le telephone est obligatoire")
        @Column(nullable = false)
        @Schema(description = "Telephone", example = "+237699000000")
        private String phone;

        @Schema(description = "Numero de compte genere", example = "ACC-1A2B3C4D5E6F", accessMode = Schema.AccessMode.READ_ONLY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Column(name = "account_number", nullable = false, updatable = false)
        private String accountNumber;

        @Schema(description = "Date de creation en UTC", accessMode = Schema.AccessMode.READ_ONLY)
        @JsonProperty(access = JsonProperty.Access.READ_ONLY)
        @Column(name = "created_at", nullable = false, updatable = false)
        private Instant createdAt;

        public BankUser() {
        }

        public BankUser(String firstName, String lastName, String email, String phone) {
                this.firstName = firstName;
                this.lastName = lastName;
                this.email = email;
                this.phone = phone;
        }

        @PrePersist
        private void prepareForInsert() {
                firstName = normalize(firstName);
                lastName = normalize(lastName);
                email = normalize(email).toLowerCase(Locale.ROOT);
                phone = normalize(phone);

                if (accountNumber == null || accountNumber.isBlank()) {
                        accountNumber = generateAccountNumber();
                }
                if (createdAt == null) {
                        createdAt = Instant.now();
                }
        }

        private String generateAccountNumber() {
                String token = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(Locale.ROOT);
                return "ACC-" + token;
        }

        private String normalize(String value) {
                return value == null ? null : value.trim();
        }

        public Long getId() {
                return id;
        }

        public void setId(Long id) {
                this.id = id;
        }

        public String getFirstName() {
                return firstName;
        }

        public void setFirstName(String firstName) {
                this.firstName = firstName;
        }

        public String getLastName() {
                return lastName;
        }

        public void setLastName(String lastName) {
                this.lastName = lastName;
        }

        public String getEmail() {
                return email;
        }

        public void setEmail(String email) {
                this.email = email;
        }

        public String getPhone() {
                return phone;
        }

        public void setPhone(String phone) {
                this.phone = phone;
        }

        public String getAccountNumber() {
                return accountNumber;
        }

        public void setAccountNumber(String accountNumber) {
                this.accountNumber = accountNumber;
        }

        public Instant getCreatedAt() {
                return createdAt;
        }

        public void setCreatedAt(Instant createdAt) {
                this.createdAt = createdAt;
        }
}