package com.inf352.bankapi.controller;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inf352.bankapi.model.BankUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRepository userRepository;

    @Test
    void createUserShouldReturnCreated() throws Exception {
        BankUser createdUser = new BankUser("Awa", "Diallo", "awa@example.com", "+237699000000");
        createdUser.setId(1L);

        when(userRepository.addUser(any(BankUser.class))).thenReturn(createdUser);

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BankUser(
                                "Awa",
                                "Diallo",
                                "awa@example.com",
                                "+237699000000"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.email").value("awa@example.com"));

        verify(userRepository).addUser(any(BankUser.class));
    }

    @Test
    void listUsersShouldReturnUsers() throws Exception {
        BankUser user1 = new BankUser("Awa", "Diallo", "awa@example.com", "+237699000000");
        user1.setId(1L);
        BankUser user2 = new BankUser("Moussa", "Kone", "moussa@example.com", "+237688000000");
        user2.setId(2L);

        when(userRepository.selectUsers()).thenReturn(List.of(user1, user2));

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("awa@example.com"))
                .andExpect(jsonPath("$[1].email").value("moussa@example.com"));
    }

    @Test
    void createUserWithInvalidEmailShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new BankUser(
                                "Awa",
                                "Diallo",
                                "bad-email",
                                "+237699000000"))))
                .andExpect(status().isBadRequest());

        verify(userRepository, never()).addUser(any(BankUser.class));
    }
}