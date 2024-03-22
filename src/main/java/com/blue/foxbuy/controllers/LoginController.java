package com.blue.foxbuy.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        if (userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()){
            return ResponseEntity.badRequest().body("Empty field/s");
        } else if (userService.findByUsername(userDTO.getUsername()) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Username not found");
        } else if (userService.findByUsername(userDTO.getUsername()).isEmailVerified == false) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    "Email not verified, please check your spam folder or click the 'Resend email verification' button");
        } else if (userService.findByUsernameAndPassword(userDTO) == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password or Username incorrect");
        }

        //String jwtToken = generateJwtToken(userDTO.getUsername());
        //return ResponseEntity.ok(new JwtResponse(jwtToken));
        return ResponseEntity.ok().body("Login successful");
    }
}
