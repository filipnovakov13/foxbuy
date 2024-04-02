package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.JwtResponseDTO;
import com.blue.foxbuy.models.DTOs.UserResultDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;

import com.blue.foxbuy.services.JwtUtilService;
import com.blue.foxbuy.services.UserService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.util.HashMap;
import java.util.Map;

@RestController
public class LoginController {

    private final UserService userService;

    private final JwtUtilService jwtUtilService;

    @Autowired
    public LoginController(UserService userService, JwtUtilService jwtUtilService) {
        this.userService = userService;
        this.jwtUtilService = jwtUtilService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        if (userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDTO("Empty field/s"));
        } else if (userService.findByUsername(userDTO.getUsername()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Username not found"));
        } else if (!userService.findByUsername(userDTO.getUsername()).isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO(
                    "Email not verified, please check your spam folder or click the 'Resend email verification' button"));
        } else if (userService.findByUsernameAndPassword(userDTO).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Password or Username incorrect"));
        }

        String jwtToken = jwtUtilService.generateJwtToken(userDTO.getUsername());
        return ResponseEntity.ok().body(new JwtResponseDTO(jwtToken));
        //return ResponseEntity.ok().body("Login successful");
    }

    @GetMapping("/test")
    public Authentication testing(Authentication authentication) {


        return authentication;
    }

    // username: adam021
    // id: 1

    @GetMapping("/auth")
    public ResponseEntity<?> testingUserIdentity(@RequestHeader(value="authorization", required = true) String authenticationHeader) {
        Map<String, String> tokenDetails = jwtUtilService.parseToken(authenticationHeader);

        if (tokenDetails.get("valid").equals("false")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Provided token is invalid."));
        }

        String username = tokenDetails.get("username");
        String userID = userService.findByUsername(username).getId().toString();

        UserResultDTO userResultDTO = new UserResultDTO();
        userResultDTO.setUsername(username);
        userResultDTO.setId(userID);
        return ResponseEntity.ok(userResultDTO);
    }
}
