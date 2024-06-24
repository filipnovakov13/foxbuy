package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.implementations.UserServiceImp;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserDetailsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserServiceImp userServiceImp;

    User user1;
    User user2;

    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();

        user1 = new User( "user1", "Password1!", "user1@example.com", true, "token1", Role.USER);
        user2 = new User("user2", "Password2!", "user2@example.com", true, "token2", Role.USER);

        userRepository.save(user1);
        userRepository.save(user2);
    }

    @Test
    @WithMockUser
    void testFindUserById_Success() throws Exception {
        UUID userId = user1.getId();

        mockMvc.perform(get("/user/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "username":"user1",
                            "email":"user1@example.com",
                            "role":"USER",
                            "ads":[]
                        }
                        """));
    }

    @Test
    @WithMockUser
    void testFindUserById_NotFound() throws Exception {
        UUID userId = UUID.randomUUID();

        mockMvc.perform(get("/user/{id}", userId))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "status":"404",
                            "message":"User with this Id doesn't exist"
                        }
                        """));
    }

    @Test
    @WithMockUser
    void testFindAllUsers_Success() throws Exception {

        mockMvc.perform(get("/user"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "page":1,
                            "totalPages":1,
                            "users":[
                                {
                                    "username":"user1",
                                    "email":"user1@example.com",
                                    "role":"USER",
                                    "adCount":0
                                },
                                {
                                    "username":"user2",
                                    "email":"user2@example.com",
                                    "role":"USER",
                                    "adCount":0
                                }
                            ]
                        }
                        """));
    }

    @Test
    @WithMockUser
    void testFindAllUsers_BadRequest() throws Exception {

        mockMvc.perform(get("/user").param("page", "5"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("""
                        {
                            "status":"400",
                            "message":"You requested page 5 but there are only 1 pages"
                        }
                        """));
    }
}
