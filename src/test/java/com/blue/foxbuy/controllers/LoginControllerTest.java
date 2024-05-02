package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.ConversionService;
import com.blue.foxbuy.services.JwtUtilService;
import com.blue.foxbuy.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LoginControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtilService jwtUtilService;

    @Autowired
    ConversionService conversionService;

    private UserDTO userDTO;

    @BeforeEach
    void setUp() throws MessagingException {
        userRepository.deleteAll();

        userDTO = new UserDTO(
                "shimmy",
                "Password1+-",
                "email@example.com");

        try {
            userService.save(userDTO);
        } catch (MessagingException e) {
            throw new MessagingException("Oops. There seems to have been an error with the e-mail messaging service. Try again or contact support for assistance.");
        }
    }

    @Test
    void loginEndpointTest_validUserCredentials_successful() throws Exception {

        mockMvc.perform(post("/login")
                        .content(conversionService.convertObjectToJson(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void loginEndpointTest_invalidUserCredentials_notFound() throws Exception {
        userDTO.setUsername("negaShimmy");

        mockMvc.perform(post("/login")
                        .content(conversionService.convertObjectToJson(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "shimmy", authorities = "ADMIN")
    void testEndpointTest_validUser_successfulAndCorrectContent() throws Exception {

        MvcResult loginResult = mockMvc.perform(post("/login")
                        .contentType("application/json")
                        .content(conversionService.convertObjectToJson(userDTO)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        JsonNode jsonNode = conversionService.readTree(responseBody);
        String returnedToken = jsonNode.get("token").asText();


        mockMvc.perform(get("/test")
                        .header("Authorization", "Bearer " + returnedToken)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    void testEndpointTest_missingJwt_unauthorized() throws Exception {

        mockMvc.perform(get("/test")
                        .content(conversionService.convertObjectToJson(userDTO)))
                .andExpect(status().isUnauthorized());
    }
}