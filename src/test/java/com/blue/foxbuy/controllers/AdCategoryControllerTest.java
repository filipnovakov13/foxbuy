package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.AdCategoryDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdCategoryService;
import com.blue.foxbuy.services.implementations.JwtUtilServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class AdCategoryControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdCategoryService adCategoryService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtilServiceImpl jwtUtilService;

    private ObjectMapper op;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        userRepository.save(new User(
                "shimmy",
                "Password1+-",
                "testing@seznam.cz",
                false,
                "emailToken",
                Role.ADMIN));
        op = new ObjectMapper();
    }

    @Test
    @WithMockUser(username = "shimmy", roles = {"ADMIN"})
    void createCategorySuccessful() throws Exception {
        AdCategoryDTO adCategoryDTO = new AdCategoryDTO("IT", "Technical gizmos and gadgets");

        mockMvc.perform(post("/category")
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "shimmy", roles = {"ADMIN"})
    void createCategoryUnsuccessful() throws Exception {
        AdCategoryDTO adCategoryDTO = new AdCategoryDTO("IT", "Technical gizmos and gadgets");

        mockMvc.perform(post("/category")
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/category")
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "shimmy", roles = {"ADMIN"})
    void updateCategorySuccessful() throws Exception {
        AdCategoryDTO adCategoryDTO = new AdCategoryDTO("IT", "Technical gizmos and gadgets");
        AdCategoryDTO updatedAdCategoryDTO = new AdCategoryDTO("IT", "All the technical toys");

        mockMvc.perform(post("/category")
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/category/{id}", 1)
                        .content(op.writeValueAsString(updatedAdCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"name\":\"IT\",\"description\":\"All the technical toys\", \"id\": 1}"));
    }

    @Test
    @WithMockUser(username = "shimmy", roles = {"ADMIN"})
    void updateCategoryUnsuccessful() throws Exception {
        AdCategoryDTO adCategoryDTO = new AdCategoryDTO("IT", "Technical gizmos and gadgets");
        AdCategoryDTO updatedAdCategoryDTO = new AdCategoryDTO("IT", "All the technical toys");

        mockMvc.perform(post("/category")
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(put("/category/{id}", 2)
                        .content(op.writeValueAsString(updatedAdCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "shimmy", roles = {"ADMIN"})
    void deleteCategorySuccessful() throws Exception {
        AdCategoryDTO adCategoryDTO = new AdCategoryDTO("IT", "Technical gizmos and gadgets");

        mockMvc.perform(post("/category")
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/category/{id}", 1)
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "shimmy", roles = {"ADMIN"})
    void deleteCategoryUnsuccessful() throws Exception {
        AdCategoryDTO adCategoryDTO = new AdCategoryDTO("IT", "Technical gizmos and gadgets");

        mockMvc.perform(post("/category")
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/category/{id}", 2)
                        .content(op.writeValueAsString(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}