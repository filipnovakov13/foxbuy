package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;

import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailVerificationRestController {
    private final UserRepository userRepository;

    @Autowired
    public EmailVerificationRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findUserByEmailVerificationToken(token);

        if (user != null) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("E-mail verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid verification token");
        }
    }
}