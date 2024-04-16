package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdService;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AdvertisementRestControllerTest {

    // Wire everything and declare MockMvc
    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdRepository adRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AdService adService;

    // Declare the variables
    private ObjectMapper op;
    String username;
    String password;
    String email;
    User user;

    // Purge databases before each test and initialize the variables,
    // the object mapper and then the user and save him into the database.
    @BeforeEach
    void setUp() {
        adRepository.deleteAll();
        userRepository.deleteAll();
        username = "mockuser";
        password = "mockpassword";
        email = "mockemail@example.com";
        user = new User(
                username,
                password,
                email,
                true,
                "mocktoken",
                Role.USER);
        userRepository.save(user);
        op = new ObjectMapper();
    }

    // Ad creation test with a positive outcome
    @Test
    @WithMockUser(username = "mockuser", roles = "USER")
    public void createAdControllerTest_validAd_returnSuccessfulResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                "12345",
                3);

        String adDTOJson = op.writeValueAsString(adDTO);

        // Here we check for an OK status
        mockMvc.perform(post("/advertisement")
                        .content(adDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Here we check whether the ad has been saved to the database
        List<Ad> ads = adRepository.findAll();
        assertThat(ads).hasSize(1);
    }

    // Ad update test with a positive outcome
    @Test
    @WithMockUser(username = "mockuser", roles = "USER")
    public void updateAdControllerTest_validAd_returnSuccessfulResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                "12345",
                3);
        AdDTO updatedAdDTO = new AdDTO(
                "Mock title update",
                "Mock description update",
                30001.00,
                "54321",
                1);

        Ad ad = adService.saveAdDTO(adDTO, user);
        String updatedAdDTOJson = op.writeValueAsString(updatedAdDTO);

        // Here we check for an OK status
        mockMvc.perform(put("/advertisement/" + ad.getId())
                        .content(updatedAdDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // Here we check whether the ad title has been updated
        List<Ad> ads = adRepository.findAll();
        Ad savedMockAd = ads.get(0);
        assertThat(savedMockAd.getTitle()).isEqualTo(updatedAdDTO.getTitle());
    }

    // Ad deletion test with a positive outcome
    @Test
    @WithMockUser(username = "mockuser", roles = "USER")
    public void deleteAdControllerTest_validAd_returnSuccessfulResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                "12345",
                3
        );

        Ad ad = adService.saveAdDTO(adDTO, user);

        // Here we check for an OK status
        mockMvc.perform(delete("/advertisement/" + ad.getId()))
                .andExpect(status().isOk());

        // Here we check whether the ad has been deleted
        List<Ad> ads = adRepository.findAll();
        assertThat(ads).hasSize(0);
    }

    // Ad creation test with a negative outcome due to limit reached
    @Test
    @WithMockUser(username = "mockuser", roles = "USER")
    public void createAdControllerTest_validAd_returnForbiddenResponse() throws Exception {
        // Here we create an AdDTO
        AdDTO adDTO1 = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                "12345",
                3);

        // We proceed to save it thrice
        adService.saveAdDTO(adDTO1, user);

        AdDTO adDTO2 = adDTO1;

        adService.saveAdDTO(adDTO2, user);

        AdDTO adDTO3 = adDTO1;

        adService.saveAdDTO(adDTO3, user);

        // We convert the AdDTO into a JSON with the object mapper
        String adDTOJson = op.writeValueAsString(adDTO1);

        // Here we check for a forbidden status due to too many ads
        // created by user role "USER"
        mockMvc.perform(post("/advertisement")
                        .content(adDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());

        // Here we check whether the ad has been saved to the database
        List<Ad> ads = adRepository.findAll();
        assertThat(ads).hasSize(3);
    }

    // Ad update test with negative outcome due to user not being the ad owner
    @Test
    public void updateAdControllerTest_validAd_returnUnauthorizedResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                "12345",
                3);
        // We create and save a second user
        User user2 = new User(
                        "mockuser2",
                        "mockpassword",
                        "mockemail2@example.com",
                        true,
                        "mocktoken",
                        Role.USER);

        userRepository.save(user2);

        AdDTO updatedAdDTO = new AdDTO(
                "Mock title update",
                "Mock description update",
                30001.00,
                "54321",
                1);
        // Here we save an ad as user no. 1
        Ad ad = adService.saveAdDTO(adDTO, user);
        String updatedAdDTOJson = op.writeValueAsString(updatedAdDTO);

        // Here we check for an unauthorized status due to the user 2
        // not being the ad owner
        mockMvc.perform(put("/advertisement/" + ad.getId())
                        .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtb2NrdXNlcjIiLCJpc3MiOiJGb3hidXkiLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3MTMyOTU5NzYsImV4cCI6MTcxMzI5OTU3Nn0.dtcimY0sbdjEBA0AziOca99AZ1dvtuDxMVIvS365dXg")
                        .content(updatedAdDTOJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());

        // Here we check whether the ad title has not been updated
        List<Ad> ads = adRepository.findAll();
        Ad savedMockAd = ads.get(0);
        assertThat(savedMockAd.getTitle()).isNotEqualTo(updatedAdDTO.getTitle());
    }

    // Ad deletion test with negative outcome due to user not being the ad owner
    @Test
    public void deleteAdControllerTest_validAd_returnUnauthorizedResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                "12345",
                3);
        User user2 = new User(
                "mockuser2",
                "mockpassword",
                "mockemail2@example.com",
                true,
                "mocktoken",
                Role.USER);

        userRepository.save(user2);

        Ad ad = adService.saveAdDTO(adDTO, user);

        // Here we check for an OK status
        mockMvc.perform(delete("/advertisement/" + ad.getId())
                        .header("Authorization", "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtb2NrdXNlcjIiLCJpc3MiOiJGb3hidXkiLCJyb2xlIjoiQURNSU4iLCJpYXQiOjE3MTMyOTU5NzYsImV4cCI6MTcxMzI5OTU3Nn0.dtcimY0sbdjEBA0AziOca99AZ1dvtuDxMVIvS365dXg"))
                .andExpect(status().isUnauthorized());

        // Here we assert the ad hasn't been deleted
        List<Ad> ads = adRepository.findAll();
        assertThat(ads).hasSize(1);
    }
}