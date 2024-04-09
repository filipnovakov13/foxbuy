package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.AdResultDTO;
import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
public class AdvertisementRestController {
    private final AdService adService;
    private final AdRepository adRepository;

    @Autowired
    public AdvertisementRestController(AdService adService, AdRepository adRepository) {
        this.adService = adService;
        this.adRepository = adRepository;
    }

    @PostMapping("/advertisement")
    public ResponseEntity<?> adCreate(@RequestBody AdDTO adDTO) {
        Ad savedAd = adService.saveAdDTO(adDTO);
        return ResponseEntity.ok().body(new AdResultDTO(savedAd));
    }

    @PutMapping("/advertisement/{id}")
    public ResponseEntity<?> adUpdate(@PathVariable UUID id, @RequestBody AdDTO adDTO) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            if (!ad.getTitle().equals(adDTO.getTitle())) {
                ad.setTitle(adDTO.getTitle());
            }
            if (!ad.getDescription().equals(adDTO.getDescription())) {
                ad.setDescription(adDTO.getDescription());
            }
            if (ad.getPrice() != (adDTO.getPrice())) {
                ad.setPrice(adDTO.getPrice());
            }
            if (ad.getCategoryID() != adDTO.getCategoryID()) {
                ad.setCategoryID(adDTO.getCategoryID());
            }
            Ad savedAd = adService.saveAd(ad);
            return ResponseEntity.ok().body(new AdResultDTO(savedAd));
        } else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/advertisement/{id}")
    public ResponseEntity<?> adDelete(@PathVariable UUID id) {
        Optional<Ad> adOptional = adRepository.findById(id);
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            adRepository.delete(ad);
            return ResponseEntity.ok().build();
        } else return ResponseEntity.notFound().build();
    }
}