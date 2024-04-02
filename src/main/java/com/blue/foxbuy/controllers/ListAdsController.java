package com.blue.foxbuy.controllers;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/list/advertisement")
public class ListAdsController {





    @GetMapping("/{id}")    // GET /advertisement /{id} – return one Ad by ID
    public ResponseEntity<?> findOneAdByID(@RequestParam int id){

    }

    @GetMapping("")         // GET /advertisement?user=user123 (list ads for specific user)
    public ResponseEntity<?> findAdsByUser(){

    }



    @GetMapping("")         // GET /advertisement?category=2&page=1 (list ads using category ID and pagination.
    // Page parameter is optional, by default is 1. Include parameters page and total_pages in response)
    public ResponseEntity<?> findAdsByCategory(){

    }

}
