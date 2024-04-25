package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserResultDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Hidden
public class EmailVerificationRestController {
    private final UserRepository userRepository;

    @Autowired
    public EmailVerificationRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@RequestParam("token") String token) {
        User user = userRepository.findUserByEmailVerificationToken(token);

        if (user != null) {
            user.setEmailVerified(true);

            userRepository.save(user);

            UserResultDTO userResultDTO = new UserResultDTO();

            userResultDTO.setUsername(user.getUsername());

            userResultDTO.setId(user.getId().toString());

            userResultDTO.setMessage("E-mail verified successfully.");

            return ResponseEntity.ok(userResultDTO);
        } else {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("401");

            errorDTO.setMessage("Invalid verification token.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
        }
    }
}