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
        usernameAdmin = "mockuser";
        passwordAdmin = "mockpassword";
        emailAdmin = "mockemail@example.com";
        user = userRepository.save(new User(
                usernameAdmin,
                passwordAdmin,
                emailAdmin,
                true,
                "mocktoken",
                Role.ADMIN));

    }

    // User ban
    @Test
    public void banUserTest_validUser_returnSuccessfulReponse() throws Exception {
        BanDTO banDTO = new BanDTO();

        banDTO.setDuration(5);
        System.out.println("/user/" + user.getId().toString() + "/ban" + " **********************************");
        System.out.println(conversionService.convertObjectToJson(banDTO) + " **********************************");

        mockMvc.perform(post( "/user/" + user.getId() + "/ban")
                        .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IkZveGJ1eSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcxMzg3Mjc0MywiZXhwIjoxNzEzODc2MzQzfQ.ReKfZNmTW67xsMaFuYaViWf1yH2tQQCGFhXKiEwQiq0")
                        .content(conversionService.convertObjectToJson(banDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}