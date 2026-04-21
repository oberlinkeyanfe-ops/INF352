package com.inf352.bankapi.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "bank_accounts")
@Schema(description = "Compte bancaire rattaché à un utilisateur")
public class BankAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column(name = "account_number", nullable = false, unique = true, updatable = false)
    @Schema(example = "ACC-1A2B3C4D5E6F")
    private String accountNumber;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 19, scale = 2)
    @Schema(example = "0.00")
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Instant createdAt;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private BankUser user;

    @OneToMany(mappedBy = "account")
    @JsonIgnore
    private List<BankTransaction> transactions = new ArrayList<>();

    public BankAccount() {
    }

    public BankAccount(BankUser user) {
        this.user = user;
    }

    @PrePersist
    private void prepareForInsert() {
        if (accountNumber == null || accountNumber.isBlank()) {
            accountNumber = generateAccountNumber();
        }
        if (createdAt == null) {
            createdAt = Instant.now();
        }
        if (balance == null) {
            balance = BigDecimal.ZERO;
        }
    }

    private String generateAccountNumber() {
        String token = UUID.randomUUID().toString().replace("-", "").substring(0, 12).toUpperCase(Locale.ROOT);
        return "ACC-" + token;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public BankUser getUser() {
        return user;
    }

    public void setUser(BankUser user) {
        this.user = user;
    }

    public List<BankTransaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BankTransaction> transactions) {
        this.transactions = transactions;
    }
}
