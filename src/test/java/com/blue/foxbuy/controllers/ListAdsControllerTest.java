package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdCategoryRepository;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdService;
import com.blue.foxbuy.services.implementations.JwtUtilServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ListAdsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdRepository adRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdCategoryRepository adCategoryRepository;

    @Autowired
    JwtUtilServiceImpl jwtUtilService;

    @Autowired
    AdService adService;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        User user = new User(
                "shimmy",
                "Password1+-",
                "testing@seznam.cz",
                true,
                "emailToken",
                Role.ADMIN);
        userRepository.save(user);

        adCategoryRepository.deleteAll();
        AdCategory adCategory = new AdCategory("IT", "I hate testing!");
        adCategoryRepository.save(adCategory);

        adRepository.deleteAll();
        adRepository.save(new Ad(
                "testing 1",
                "more testing 1",
                100.0,
                "12345",
                user,
                adCategory
        ));
        adRepository.save(new Ad(
                "testing 2",
                "more testing 2",
                100.0,
                "12345",
                user,
                adCategory
        ));
    }

    @Test
    void findOneAdById_successful() throws Exception {
        List<Ad> ad = adRepository.findAll();
        mockMvc.perform(get("/advertisement/{id}", ad.get(0).getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("testing 1"))
                .andExpect(jsonPath("$.description").value("more testing 1"));
    }

    @Test
    void findAdsByUser_successful() throws Exception {
        mockMvc.perform(get("/advertisement/user")
                .param("username", "shimmy")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.ads").isArray())
                .andExpect(jsonPath("$.ads[0].title").value("testing 1"))
                .andExpect(jsonPath("$.ads[1].description").value("more testing 2"));
    }

    @Test
    void findAdsByCategory_successful() throws Exception {
        AdCategory adCategory = adCategoryRepository.findAdCategoryByNameIgnoreCase("IT");
        mockMvc.perform(get("/advertisement/category")
                .param("categoryId", adCategory.getId().toString())
                .param("page", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.ads").isArray());
    }

}
