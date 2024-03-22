package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.UserResultDTO;
import com.blue.foxbuy.services.EmailService;
import com.blue.foxbuy.services.UserService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO) throws MessagingException {
        if (!userService.isEmailValid(userDTO.getEmail()) || !userService.isPasswordValid(userDTO.getPassword())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Invalid Format"));
        }
        if (userService.isUsernameInUse(userDTO.getUsername()) || userService.isEmailInUse(userDTO.getEmail())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Username or email is already in use"));
        }

        return ResponseEntity.ok().body(new UserResultDTO(userService.save(userDTO).getId().toString(), userDTO.getUsername()));
    }

}
