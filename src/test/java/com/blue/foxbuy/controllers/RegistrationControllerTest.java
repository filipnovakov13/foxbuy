package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.ConversionService;
import com.blue.foxbuy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class RegistrationControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ConversionService conversionService;

    private UserDTO userDTO;


    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        userDTO = new UserDTO(
                "shimmy",
                "Password1+-",
                "email@example.com");
    }


    @Test
    public void registrationEndpointTest_validUser_successfulAndUserPresent() throws Exception {

        mockMvc.perform(post("/registration")
                        .content(conversionService.convertObjectToJson(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        List<User> users = userRepository.findAll();

        assertThat(users).hasSize(1);
    }

    @Test
    public void registrationEndpointTest_existingUser_badRequest() throws Exception {
        userService.save(userDTO);

        mockMvc.perform(post("/registration")
                        .content(conversionService.convertObjectToJson(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
