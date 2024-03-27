package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class RegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    private ObjectMapper op;


    @BeforeEach
    void setUp() {
        op = new ObjectMapper();
        userRepository.deleteAll();
    }


    @Test
    public void registrationEndpointSuccessful() throws Exception {
        UserDTO userDTO = new UserDTO("shimmy", "Password1+-", "testing@gmail.com");

        mockMvc.perform(post("/registration")
                        .content(op.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void registrationEndpointUnauthorized() throws Exception {
        UserDTO userDTO = new UserDTO("shimmy", "password", "testing@gmail.com");

        mockMvc.perform(post("/registration")
                        .content(op.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}
