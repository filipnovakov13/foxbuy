package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class EmailVerificationRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        User user = new User(
                "shimmy",
                "Password1+-",
                "testing@seznam.cz",
                false,
                "emailToken");
        userRepository.save(user);
    }

    @Test
    public void verifyEmailWithValidToken() throws Exception {
        mockMvc.perform(get("/verify-email")
                        .param("token", "emailToken"))
                .andExpect(status().isOk())
                .andExpect(content().string("E-mail verified successfully"));

        User user = userRepository.findByUsername("shimmy");
        assertTrue(user.isEmailVerified());
    }
}