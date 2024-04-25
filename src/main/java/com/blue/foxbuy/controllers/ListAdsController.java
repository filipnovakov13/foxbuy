package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserResultDTO;
import com.blue.foxbuy.services.AdService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/advertisement")
@Tag(name = "Ad lists")
public class ListAdsController {

    private final AdService adservice;

    @Operation(
            description = "Finds an ad based on an id.",
            summary = "Finds ad.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "400",
                            description = "Returned when the id doesn't match any ad id's in the database.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"400\"," +
                                            "\"message\": \"Ad not found.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when an ad id matches the provided id.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResultDTO.class),
                                    examples = @ExampleObject(value = "{\n" +
                                            "    \"title\": \"iPhone 15 Pro Max 1TB\",\n" +
                                            "    \"description\": \"Selling my 2 months old iPhone. 22 month warranty. Original box included.\",\n" +
                                            "    \"price\": 41000.0,\n" +
                                            "    \"zipcode\": \"12345\",\n" +
                                            "    \"categoryID\": 1\n" +
                                            "}")
                            )
                    )
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<?> findOneAdById(@PathVariable(required = false) final UUID id) {
        return ResponseEntity.ok(adservice.getAdById(id));
    }

    @GetMapping("/user")
    public ResponseEntity<?> findAdsByUser(@RequestParam final String username) {
        return ResponseEntity.ok(adservice.getAdsByUser(username));
    }

    @GetMapping("/category")
    public ResponseEntity<?> findAdsByCategory(@RequestParam final Integer categoryId, @RequestParam(required = false, defaultValue = "0") final Integer page) {
            return ResponseEntity.ok(adservice.getAdsByCategory(categoryId, page));
    }
}