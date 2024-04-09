package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.AdResultDTO;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.services.AdService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
@ActiveProfiles("test")
class AdvertisementRestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AdRepository adRepository;

    @Autowired
    AdService adService;

    private ObjectMapper op;

    @BeforeEach
    void setUp() {
        adRepository.deleteAll();
        op = new ObjectMapper();
    }

    @Test
    public void createAdControllerTest_validAd_returnSuccessfulResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                12345,
                3
        );

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

    @Test
    public void updateAdControllerTest_validAd_returnSuccessfulResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                12345,
                3
        );

        AdDTO updatedAdDTO = new AdDTO(
                "Mock title update",
                "Mock description update",
                30001.00,
                54321,
                1
        );

        Ad ad = adService.saveAdDTO(adDTO);
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

    @Test
    public void deleteAdControllerTest_validAd_returnSuccessfulResponse() throws Exception {
        AdDTO adDTO = new AdDTO(
                "Mock title",
                "Mock description",
                3000.00,
                12345,
                3
        );

        Ad ad = adService.saveAdDTO(adDTO);

        // Here we check for an OK status
        mockMvc.perform(delete("/advertisement/" + ad.getId()))
                .andExpect(status().isOk());

        // Here we check whether the ad has been deleted
        List<Ad> ads = adRepository.findAll();
        assertThat(ads).hasSize(0);
    }

}