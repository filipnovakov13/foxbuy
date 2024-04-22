package com.blue.foxbuy.controllers;


import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.services.AdService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/advertisement")
public class ListAdsController {

    private final AdService adservice;


    @GetMapping("/{id}")
    public ResponseEntity<?> findOneAdById(@PathVariable (required = false) final UUID id){
        try {
            return ResponseEntity.ok(adservice.getAdById(id));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> findAdsByUser(@RequestParam final String username){
        try {
            return ResponseEntity.ok(adservice.getAdsByUser(username));
        } catch (RuntimeException e){
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    @GetMapping("")
    public ResponseEntity<?> findAdsByCategory(@RequestParam final int categoryId, @RequestParam (required = false, defaultValue = "1") final int page){
        try {
            return ResponseEntity.ok(adservice.getAdsByCategory(categoryId, page));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDTO> throwCustomException(RuntimeException e){
        return ResponseEntity.badRequest().body(new ErrorDTO(e.getMessage()));
    }
}
