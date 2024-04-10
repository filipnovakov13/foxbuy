package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.JwtUtilService;
import com.blue.foxbuy.services.UserService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class LoginControllerTest {
    
    @Autowired
    MockMvc mockMvc;
    
    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;
    
    @Autowired
    JwtUtilService jwtUtilService;

    private ObjectMapper op;
    
    @BeforeEach
    void setUp() throws MessagingException {
        op = new ObjectMapper();
        userRepository.deleteAll();
        UserDTO userDTO = new UserDTO("Adam021", "password1!", "adam021@gmail.com");
        try {
            userService.save(userDTO);
        } catch (MessagingException e) {
            throw new MessagingException(e.getMessage());
        }
    }
    
    @Test
    public void LoginEndpointSuccessful() throws Exception {
        UserDTO userDTO = new UserDTO("Adam021", "password1!", "adam021@gmail.com");

        mockMvc.perform(post("/login")
                .content(op.writeValueAsString(userDTO))
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void LoginEndpointUnsuccessful() throws Exception {
        UserDTO userDTO = new UserDTO("Adam021", "password2!", "adam021@gmail.com");

        mockMvc.perform(post("/login")
                        .content(op.writeValueAsString(userDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void TestEndpointSuccessful() throws Exception {
        UserDTO userDTO = new UserDTO("Adam021", "password1!", "adam021@gmail.com");
        String token = jwtUtilService.generateJwtToken(userDTO.getUsername());

        MvcResult loginResult = mockMvc.perform(post("/login")
                            .header("Authorization", "Bearer " + token)
                            .contentType("application/json")
                            .content(op.writeValueAsString(userDTO)))
                        .andExpect(status().isOk())
                        .andExpect(content().contentType("application/json"))
                        .andReturn();

        String responseBody = loginResult.getResponse().getContentAsString();
        JsonNode jsonNode = op.readTree(responseBody);
        String returnedToken = jsonNode.get("token").asText();


        mockMvc.perform(get("/test")
                    .header("Authorization","Bearer " + returnedToken)
                    .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void TestEndpointUnsuccessful() throws Exception {
        UserDTO userDTO = new UserDTO("Adam021", "password1!", "adam021@gmail.com");
        mockMvc.perform(get("/test")
                        .content(op.writeValueAsString(userDTO)))
                .andExpect(status().isUnauthorized());
    }
}