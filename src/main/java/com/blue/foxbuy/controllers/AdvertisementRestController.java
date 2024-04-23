package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.AdResultDTO;
import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.AdService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
public class AdvertisementRestController {

    private final AdService adService;
    private final AdRepository adRepository;
    private final UserRepository userRepository;

    @Autowired
    public AdvertisementRestController(AdService adService, AdRepository adRepository, UserRepository userRepository) {
        this.adService = adService;
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/advertisement")
    public ResponseEntity<?> adCreate(@Valid @RequestBody AdDTO adDTO, Authentication authentication) {
        // First we retrieve the username of the user from the authentication object
        // that was created by the JWT validation filter. Then we get the user UUID
        // from the database and save it within the Ad object itself.
        String username;
        try {
            username = authentication.getName();
        } catch (NullPointerException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Authentication error. Please, log out then log in and try again."));
        }

        User user = userRepository.findByUsername(username);

        // Before creating the ad though, we check whether the user is an admin
        // a vip or has less than 3 ads created thus far. Only then will he receive
        // a 200 response otherwise
        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals((Role.VIP_USER)) || adService.canUserCreateAd(user)) {
            Ad savedAd = adService.saveAdDTO(adDTO, user);
            return ResponseEntity.ok().body(new AdResultDTO(savedAd));
        } else
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO("User has reached the maximum ad limit. Upgrade to VIP for unlimited ads."));
    }

    @PutMapping("/advertisement/{id}")
    public ResponseEntity<?> adUpdate(@PathVariable UUID id, @RequestBody AdDTO adDTO, Authentication authentication) {
        // Here we save the ad as an optional because we have no idea if it exists.
        // If we get a null then we send a 404 "not found" response but if it does
        // then we check if the user is an admin or is the owner by comparing the
        // username in the jwt together with the username registered to the UUID
        // within the ad.
        Optional<Ad> adOptional = adRepository.findById(id);

        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            UUID adOwnerID = ad.getOwner().getId();
            String username;
            try {
                username = authentication.getName();
            } catch (NullPointerException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Authentication error. Please, log out then log in and try again."));
            }
            User user = userRepository.findByUsername(username);

            if (user.getRole().equals(Role.ADMIN) || adOwnerID.equals(user.getId())) {
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
            } else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("User not authorized to make changes to this ad."));
        } else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/advertisement/{id}")
    public ResponseEntity<?> adDelete(@PathVariable UUID id, Authentication authentication) {
        // Same ordeal as with the adUpdate method upstairs
        Optional<Ad> adOptional = adRepository.findById(id);

        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            UUID adOwnerID = ad.getOwner().getId();
            String username;
            try {
                username = authentication.getName();
            } catch (NullPointerException e) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Authentication error. Please, log out then log in and try again."));
            }
            User user = userRepository.findByUsername(username);

            if (user.getRole().equals(Role.ADMIN) || adOwnerID.equals(user.getId())) {
                adRepository.delete(ad);
                return ResponseEntity.ok().build();
            } else
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("You are not authorized to delete this ad."));
        } else return ResponseEntity.notFound().build();
    }


}