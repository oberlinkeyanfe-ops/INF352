package com.inf352.bankapi.controller;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inf352.bankapi.model.BankAccount;
import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.service.BankAccountService;
import com.inf352.bankapi.controller.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
@AutoConfigureMockMvc(addFilters = false)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankAccountService bankAccountService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void createAccountShouldReturnAccount() throws Exception {
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setAccountNumber("ACC-123456789ABC");
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(Instant.parse("2026-04-16T00:00:00Z"));
        account.setUser(new BankUser());

        when(bankAccountService.createAccount(1L)).thenReturn(account);

        mockMvc.perform(post("/api/users/1/accounts"))
                .andExpect(status().isOk())
            .andExpect(jsonPath("$.accountNumber").value("ACC-123456789ABC"));
    }

    @Test
    void listAccountsShouldReturnAccounts() throws Exception {
        BankAccount account = new BankAccount();
        account.setId(1L);
        account.setAccountNumber("ACC-123456789ABC");
        account.setBalance(BigDecimal.ZERO);
        account.setCreatedAt(Instant.parse("2026-04-16T00:00:00Z"));
        account.setUser(new BankUser());

        when(bankAccountService.listAccountsForUser(eq(1L))).thenReturn(List.of(account));

        mockMvc.perform(get("/api/users/1/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].accountNumber").value("ACC-123456789ABC"));
    }
}
