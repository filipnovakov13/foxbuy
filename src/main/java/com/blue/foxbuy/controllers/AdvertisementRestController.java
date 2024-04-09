package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.AdResultDTO;
import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AdvertisementRestController {
    private final AdService adService;

    @Autowired
    public AdvertisementRestController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping("/advertisement")
    public ResponseEntity<?> adCreation(@RequestBody AdDTO adDTO) {
            Ad savedAd = adService.save(adDTO);
            return ResponseEntity.ok().body(new AdResultDTO(savedAd));
    }
}