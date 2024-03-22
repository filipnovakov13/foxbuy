package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    private final UserService userService;

    @Autowired
    public LoginController(UserService userService) {
        this.userService = userService;
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
        } else if (userService.findByUsernameAndPassword(userDTO) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Password or Username incorrect"));
        }

        //String jwtToken = generateJwtToken(userDTO.getUsername());
        //return ResponseEntity.ok(new JwtResponse(jwtToken));
        return ResponseEntity.ok().body("Login successful");
    }
}
