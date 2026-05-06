package com.inf352.bankapi.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.model.BankTransaction;
import com.inf352.bankapi.model.TransactionType;
import com.inf352.bankapi.controller.UserRepository;
import com.inf352.bankapi.exception.InsufficientFundsException;
import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.service.BankAccountService;
import com.inf352.bankapi.service.BankTransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@AutoConfigureMockMvc(addFilters = false)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankTransactionService bankTransactionService;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void depositShouldReturnTransaction() throws Exception {
        BankTransaction transaction = new BankTransaction();
        transaction.setId(1L);
        transaction.setType(TransactionType.DEPOSIT);
        transaction.setAmount(BigDecimal.valueOf(50));
        transaction.setBeforeBalance(BigDecimal.ZERO);
        transaction.setAfterBalance(BigDecimal.valueOf(50));
        transaction.setCreatedAt(Instant.parse("2026-04-16T00:00:00Z"));
        transaction.setAccount(new BankAccount());

        when(bankTransactionService.deposit(eq("ACC-123456789ABC"), eq(BigDecimal.valueOf(50))))
                .thenReturn(transaction);

        Map<String, BigDecimal> request = Map.of("amount", BigDecimal.valueOf(50));

        mockMvc.perform(post("/api/transactions/deposit/ACC-123456789ABC")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("DEPOSIT"))
                .andExpect(jsonPath("$.afterBalance").value(50));
    }

            @Test
            void depositShouldReturnNotFound() throws Exception {
            when(bankTransactionService.deposit(eq("ACC-404"), eq(BigDecimal.valueOf(50))))
                .thenThrow(new ResourceNotFoundException("Compte introuvable"));

            Map<String, BigDecimal> request = Map.of("amount", BigDecimal.valueOf(50));

            mockMvc.perform(post("/api/transactions/deposit/ACC-404")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
            }

            @Test
            void depositShouldFailValidation() throws Exception {
            Map<String, BigDecimal> request = Map.of("amount", BigDecimal.valueOf(-1));

            mockMvc.perform(post("/api/transactions/deposit/ACC-123456789ABC")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
            }

            @Test
            void withdrawShouldReturnTransaction() throws Exception {
            BankTransaction transaction = new BankTransaction();
            transaction.setId(2L);
            transaction.setType(TransactionType.WITHDRAWAL);
            transaction.setAmount(BigDecimal.valueOf(40));
            transaction.setBeforeBalance(BigDecimal.valueOf(100));
            transaction.setAfterBalance(BigDecimal.valueOf(60));
            transaction.setCreatedAt(Instant.parse("2026-04-16T00:00:00Z"));
            transaction.setAccount(new BankAccount());

            when(bankTransactionService.withdraw(eq("ACC-123456789ABC"), eq(BigDecimal.valueOf(40))))
                .thenReturn(transaction);

            Map<String, BigDecimal> request = Map.of("amount", BigDecimal.valueOf(40));

            mockMvc.perform(post("/api/transactions/withdraw/ACC-123456789ABC")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("WITHDRAWAL"))
                .andExpect(jsonPath("$.afterBalance").value(60));
            }

            @Test
            void withdrawShouldReturnNotFound() throws Exception {
            when(bankTransactionService.withdraw(eq("ACC-404"), eq(BigDecimal.valueOf(40))))
                .thenThrow(new ResourceNotFoundException("Compte introuvable"));

            Map<String, BigDecimal> request = Map.of("amount", BigDecimal.valueOf(40));

            mockMvc.perform(post("/api/transactions/withdraw/ACC-404")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
            }

            @Test
            void withdrawShouldReturnInsufficientFunds() throws Exception {
            when(bankTransactionService.withdraw(eq("ACC-123456789ABC"), eq(BigDecimal.valueOf(5000))))
                .thenThrow(new InsufficientFundsException("Solde insuffisant"));

            Map<String, BigDecimal> request = Map.of("amount", BigDecimal.valueOf(5000));

            mockMvc.perform(post("/api/transactions/withdraw/ACC-123456789ABC")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
            }

            @Test
            void withdrawShouldFailValidation() throws Exception {
            Map<String, BigDecimal> request = Map.of("amount", BigDecimal.ZERO);

            mockMvc.perform(post("/api/transactions/withdraw/ACC-123456789ABC")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
            }
}
