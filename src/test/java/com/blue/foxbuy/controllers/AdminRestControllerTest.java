package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.BanDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.ConversionService;
import com.blue.foxbuy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AdminRestControllerTest {
    // Wire everything and declare MockMvc
    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdRepository adRepository;

    @Autowired
    UserService userService;

    @Autowired
    ConversionService conversionService;

    String username;
    String password;
    String email;
    String usernameAdmin;
    String passwordAdmin;
    String emailAdmin;
    User user;
    User admin;
    Ad ad;

    @BeforeEach
    void setUp() {
        adRepository.deleteAll();

        userRepository.deleteAll();

        username = "mockuser";
        password = "mockpassword";
        email = "mockemail@example.com";
        user = userRepository.save(new User(
                username,
                password,
                email,
                true,
                "mocktoken",
                Role.USER));
    }

    // User ban
    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void banUserTest_validUser_validAdmin_successful() throws Exception {
        BanDTO banDTO = new BanDTO();

        banDTO.setDuration(5);

        mockMvc.perform(post( "/user/" + user.getId() + "/ban")
                        .content(conversionService.convertObjectToJson(banDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "admin", authorities = "ADMIN")
    public void banUserTest_validUser_expiredAdminToken_successful() throws Exception {
        BanDTO banDTO = new BanDTO();

        banDTO.setDuration(5);

        mockMvc.perform(post( "/user/" + user.getId() + "/ban")
                        .header("Authorization", "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IkZveGJ1eSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcxMzk0NTY2MiwiZXhwIjoxNzEzOTQ5MjYyfQ.XgH6QqrVCyTqrzy-JkVJDtsPF_HPJmHo8fz3rifHuPo")
                        .content(conversionService.convertObjectToJson(banDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }
}