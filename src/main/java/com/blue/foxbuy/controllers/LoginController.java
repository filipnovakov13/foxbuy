package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.JwtResponseDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.services.JwtUtilService;
import com.blue.foxbuy.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Login")
public class LoginController {

    private final UserService userService;

    private final JwtUtilService jwtUtilService;

    @Autowired
    public LoginController(UserService userService, JwtUtilService jwtUtilService) {
        this.userService = userService;
        this.jwtUtilService = jwtUtilService;
    }

    @Operation(
            description = "Accepts form inputs (username and password)" +
                    " and checks for their validity",
            summary = "User login endpoint",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "400",
                                 description = "Returned when the username or password is missing",
                                 content = @Content(
                                         mediaType = "application/json",
                                         schema = @Schema(implementation = ErrorDTO.class),
                                         examples = @ExampleObject(value = "{'error' : 'Empty field/s'}")
                                 )),
                    @ApiResponse(responseCode = "404",
                            description = "Returned when the username is not found in the database",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Username not found'}")
                            )),
                    @ApiResponse(responseCode = "403",
                            description = "Returned when the user has not finished email verification",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Email not verified," +
                                            " please check your spam folder or click the 'Resend email" +
                                            " verification' button'}")
                            )),
                    @ApiResponse(responseCode = "401",
                            description = "Returned when the username and password don't match",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Password or Username incorrect'}")
                            )),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the login is successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponseDTO.class),
                                    examples = @ExampleObject(value = "{'token' : 'eyJhbGciOiJIUzI1NiJ9." +
                                            "eyJzdWIiOiJBZGFtMDIxIiwiaXNzIjoiRm94YnV5Iiwicm9sZSI6IkFETUl" +
                                            "OIiwiaWF0IjoxNzEyMTc2OTI1LCJleHAiOjE3MTIxODA1MjV9." +
                                            "rNwsoSHQ3A-wxJKwm9oZHQa-d6KVJcH5AAJaUzaKTsU'}")
                            ))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO){
        if (userDTO.getUsername().isEmpty() || userDTO.getPassword().isEmpty()){
            return ResponseEntity.badRequest().body(new ErrorDTO("Empty field/s"));
        } else if (userService.findByUsername(userDTO.getUsername()) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDTO("Username not found"));
        } else if (!userService.findByUsername(userDTO.getUsername()).isEmailVerified()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorDTO(
                    "Email not verified, please check your spam folder or click the 'Resend email verification' button"));
        } else if (userService.findByUsernameAndPassword(userDTO).isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Password or Username incorrect"));
        }

        String jwtToken = jwtUtilService.generateJwtToken(userDTO.getUsername());
        return ResponseEntity.ok().body(new JwtResponseDTO(jwtToken));
    }

    @GetMapping("/test")
    @SecurityRequirement(name = "BearerToken")
    public Authentication testing(Authentication authentication) {


        return authentication;
    }
}
