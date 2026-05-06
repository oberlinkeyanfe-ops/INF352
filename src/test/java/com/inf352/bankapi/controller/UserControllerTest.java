package com.inf352.bankapi.controller;

import java.time.Instant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inf352.bankapi.exception.ResourceNotFoundException;
import com.inf352.bankapi.model.BankUser;
import com.inf352.bankapi.service.BankUserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankUserService bankUserService;

        @MockBean
        private UserRepository userRepository;

    @Test
    void createUserShouldReturnCreatedUser() throws Exception {
        BankUser user = new BankUser("Awa", "Diallo", "awa@example.com", "+237699000000");
        user.setId(1L);
        user.setAccountNumber("ACC-123456789ABC");
        user.setCreatedAt(Instant.parse("2026-04-16T00:00:00Z"));

        when(bankUserService.createUser(any(BankUser.class))).thenReturn(user);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("awa@example.com"))
                .andExpect(jsonPath("$.accountNumber").value("ACC-123456789ABC"));
    }

    @Test
    void createUserShouldFailValidation() throws Exception {
        BankUser user = new BankUser("Awa", "Diallo", "bad-email", "+237699000000");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateUserShouldReturnUpdatedUser() throws Exception {
        BankUser incoming = new BankUser("Awa", "Diallo", "awa2@example.com", "+237699000000");
        BankUser updated = new BankUser("Awa", "Diallo", "awa2@example.com", "+237699000000");
        updated.setId(1L);
        updated.setAccountNumber("ACC-123456789ABC");
        updated.setCreatedAt(Instant.parse("2026-04-16T00:00:00Z"));

        when(bankUserService.updateUser(eq(1L), any(BankUser.class))).thenReturn(updated);

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("awa2@example.com"));
    }

    @Test
    void updateUserShouldReturnNotFound() throws Exception {
        BankUser incoming = new BankUser("Awa", "Diallo", "awa2@example.com", "+237699000000");

        when(bankUserService.updateUser(eq(404L), any(BankUser.class)))
                .thenThrow(new ResourceNotFoundException("Utilisateur introuvable"));

        mockMvc.perform(put("/api/users/404")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserShouldFailValidation() throws Exception {
        BankUser incoming = new BankUser("Awa", "Diallo", "bad-email", "+237699000000");

        mockMvc.perform(put("/api/users/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(incoming)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deleteUserShouldReturnNoContent() throws Exception {
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteUserShouldReturnNotFound() throws Exception {
        doThrow(new ResourceNotFoundException("Utilisateur introuvable"))
                .when(bankUserService).deleteUser(eq(404L));

        mockMvc.perform(delete("/api/users/404"))
                .andExpect(status().isNotFound());
    }
}
