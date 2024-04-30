package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.DTOs.AdCategoryDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdCategoryRepository;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdCategoryService;
import com.blue.foxbuy.services.ConversionService;
import com.blue.foxbuy.services.implementations.JwtUtilServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

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
    AdCategoryRepository adCategoryRepository;

    @Autowired
    JwtUtilServiceImpl jwtUtilService;

    @Autowired
    ConversionService conversionService;

    User user;

    AdCategoryDTO adCategoryDTO;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();

        adCategoryRepository.deleteAll();

        user = new User(
                "shimmy",
                "Password1+-",
                "testing@seznam.cz",
                false,
                "emailToken",
                Role.ADMIN);

        userRepository.save(user);

        adCategoryDTO = new AdCategoryDTO(
                "IT",
                "Technical gizmos and gadgets");
    }

    @Test
    @WithMockUser(username = "shimmy", authorities = "ADMIN")
    void createCategory_successful() throws Exception {

        mockMvc.perform(post("/category")
                        .content(conversionService.convertObjectToJson(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = "shimmy", authorities = "ADMIN")
    void createCategory_conflict() throws Exception {


        mockMvc.perform(post("/category")
                        .content(conversionService.convertObjectToJson(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/category")
                        .content(conversionService.convertObjectToJson(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    @WithMockUser(username = "shimmy", authorities = {"ADMIN"})
    void updateCategory_successful() throws Exception {
        AdCategory adCategory = adCategoryService.saveAdCategoryDTO(adCategoryDTO);
        AdCategoryDTO updatedAdCategoryDTO = new AdCategoryDTO("IT", "All the technical toys");

        mockMvc.perform(put("/category/" + adCategory.getId(), 1)
                        .content(conversionService.convertObjectToJson(updatedAdCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("{\"id\":" + adCategory.getId() + ",\"name\":\"IT\",\"description\":\"All the technical toys\"}"));
    }

    @Test
    @WithMockUser(username = "shimmy", authorities = {"ADMIN"})
    void updateCategory_notFound() throws Exception {
        AdCategory adCategory = adCategoryService.saveAdCategoryDTO(adCategoryDTO);
        AdCategoryDTO updatedAdCategoryDTO = new AdCategoryDTO("Rubbish", "All types of garbage.");

        mockMvc.perform(put("/category/" + "5", 2)
                        .content(conversionService.convertObjectToJson(updatedAdCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @WithMockUser(username = "shimmy", authorities = {"ADMIN"})
    void deleteCategory_successful() throws Exception {
        AdCategory adCategory = adCategoryService.saveAdCategoryDTO(adCategoryDTO);

        mockMvc.perform(delete("/category/" + adCategory.getId(), 1))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "shimmy", authorities = {"ADMIN"})
    void deleteCategory_notFound() throws Exception {


        mockMvc.perform(post("/category")
                        .content(conversionService.convertObjectToJson(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());

        mockMvc.perform(delete("/category/{id}", 2)
                        .content(conversionService.convertObjectToJson(adCategoryDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}