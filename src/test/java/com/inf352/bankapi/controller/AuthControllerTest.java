package com.inf352.bankapi.controller;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inf352.bankapi.controller.UserRepository;
import com.inf352.bankapi.service.BankUserService;
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

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BankUserService bankUserService;

    @MockBean
    private UserRepository userRepository;

    @Test
    void issueTokenShouldReturnToken() throws Exception {
        when(bankUserService.issueToken(eq("awa@example.com"), eq("+237699000000")))
            .thenReturn("test-token");

        Map<String, String> request = Map.of(
            "email", "awa@example.com",
            "phone", "+237699000000");

        mockMvc.perform(post("/api/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("test-token"));
    }
}
