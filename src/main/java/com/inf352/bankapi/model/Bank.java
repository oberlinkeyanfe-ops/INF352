package com.inf352.bankapi.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Bank {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private Double profitRate;

    @Column(nullable = false)
    private Double maxWithdrawalPerTransaction = 500000.0;

    @Column(nullable = false)
    private Double maxTransferPerTransaction = 1000000.0;

    public Bank() {}

    public Bank(String name, Double profitRate) {
        this.name = name;
        this.profitRate = profitRate;
    }

    public Bank(String name, Double profitRate, Double maxWithdrawal) {
        this.name = name;
        this.profitRate = profitRate;
        this.maxWithdrawalPerTransaction = maxWithdrawal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getProfitRate() {
        return profitRate;
    }

    public void setProfitRate(Double profitRate) {
        this.profitRate = profitRate;
    }

    public Double getMaxWithdrawalPerTransaction() {
        return maxWithdrawalPerTransaction;
    }

    public void setMaxWithdrawalPerTransaction(Double maxWithdrawalPerTransaction) {
        this.maxWithdrawalPerTransaction = maxWithdrawalPerTransaction;
    }

    public Double getMaxTransferPerTransaction() {
        return maxTransferPerTransaction;
    }

    public void setMaxTransferPerTransaction(Double maxTransferPerTransaction) {
        this.maxTransferPerTransaction = maxTransferPerTransaction;
    }
}