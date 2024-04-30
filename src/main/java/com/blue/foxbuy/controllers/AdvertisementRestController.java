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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;

import java.util.*;

@RestController
@RequestMapping("/advertisement")
@Tag(name = "Ads")
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

    @Operation(
            description = "An endpoint used to create ads.",
            summary = "Creates ads.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Returned if ad is created successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdResultDTO.class),
                                    examples = @ExampleObject(value = "{\n" +
                                            "    \"id\": \"05b60058-7995-48bb-9a96-a40e0716c276\",\n" +
                                            "    \"title\": \"iPhone 15 Pro Max 1TB\",\n" +
                                            "    \"description\": \"Selling my two month old iPhone. Still under warranty and top shape." +
                                            "    \"price\": 41000.0,\n" +
                                            "    \"zipcode\": \"12345\",\n" +
                                            "    \"categoryID\": 1\n" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "403",
                            description = "Returned if the user's maximum ad limit is reached.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"403\"," +
                                            "\"message\": \"User has reached the maximum number of ads limit. Upgrade to VIP for unlimited ads or delete an ad to add a new one.\"" +
                                            "}")
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<?> adCreate(@Valid @RequestBody AdDTO adDTO, Authentication authentication) {
        // First we retrieve the username of the user from the authentication object
        // that was created by the JWT validation filter. Then we get the user UUID
        // from the database and save it within the Ad object itself.
        String username = authentication.getName();

        User user = userRepository.findByUsername(username);

        // Before creating the ad though, we check whether the user is an admin
        // a vip or has less than 3 ads created thus far. Only then will he receive
        // a 200 response otherwise
        if (user.getRole().equals(Role.ADMIN) || user.getRole().equals((Role.VIP_USER)) || adService.canUserCreateAd(user)) {
            Ad savedAd = adService.saveAdDTO(adDTO, user);

            return ResponseEntity.status(HttpStatus.CREATED).body(new AdResultDTO(savedAd));
        } else {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("403");

            errorDTO.setMessage("User has reached the maximum number of ads limit. Upgrade to VIP for unlimited ads or delete an ad to add a new one.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
        }
    }

    @Operation(
            description = "An endpoint used to update ads.",
            summary = "Updates ads.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned if ad is updated successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AdResultDTO.class),
                                    examples = @ExampleObject(value = "{\n" +
                                            "\"id\": \"05b60058-7995-48bb-9a96-a40e0716c276\",\n" +
                                            "  \"title\": \"iPhone 15 Pro Max 1TB\",\n" +
                                            "  \"description\": \"I'm selling my 2 months old, barely used iPhone 15 Pro Max with 1TB of storage. It's in great condition. Original box and clear Magsafe cover included.\",\n" +
                                            "  \"price\": 41000,\n" +
                                            "  \"zipcode\": \"12345\",\n" +
                                            "  \"categoryID\": 1\n" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Returned if the user's token is invalid.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"401\"," +
                                            "\"message\": \"Authorization error. Please, log out then log in and try again.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Returned if the user's not the owner of the ad.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"401\"," +
                                            "\"message\": \"User not authorized to make changes to this ad.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Returned if the ad was not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"404\"," +
                                            "\"message\": \"Ad not found.\"" +
                                            "}")
                            )
                    )
            }
    )
    @PutMapping("/{id}")
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
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setStatus("401");

                errorDTO.setMessage("Authorization error. Please, log out then log in and try again.");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
            }
            User user = userRepository.findByUsername(username);

            if (user.getRole().equals(Role.ADMIN) || adOwnerID.equals(user.getId())) {

                ad.setTitle(adDTO.getTitle());

                ad.setDescription(adDTO.getDescription());

                ad.setPrice(adDTO.getPrice());

                ad.getAdCategory().setId(adDTO.getCategoryID());

                Ad savedAd = adService.saveAd(ad);

                return ResponseEntity.ok().body(new AdResultDTO(savedAd));
            } else {
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setStatus("401");

                errorDTO.setMessage("User not authorized to make changes to this ad.");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
            }

        } else {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("404");

            errorDTO.setMessage("Ad not found.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }

    @Operation(
            description = "An endpoint used to update ads.",
            summary = "Updates ads.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "200",
                            description = "Returned if ad is updated successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema()
                            )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Returned if the user's token is invalid.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"401\"," +
                                            "\"message\": \"Authorization error. Please, log out then log in and try again.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "401",
                            description = "Returned if the user's not the owner of the ad.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"401\"," +
                                            "\"message\": \"User not authorized to make changes to this ad.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "404",
                            description = "Returned if the ad was not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"404\"," +
                                            "\"message\": \"Ad not found.\"" +
                                            "}")
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
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
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setStatus("401");

                errorDTO.setMessage("Authorization error. Please, log out then log in and try again.");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
            }
            User user = userRepository.findByUsername(username);

            if (user.getRole().equals(Role.ADMIN) || adOwnerID.equals(user.getId())) {
                adRepository.delete(ad);
                return ResponseEntity.ok().build();
            } else {
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setStatus("401");

                errorDTO.setMessage("You are not authorized to delete this ad.");

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
            }
        } else {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("404");

            errorDTO.setMessage("Ad not found.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
        }
    }
}