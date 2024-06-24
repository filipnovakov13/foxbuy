package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.JwtResponseDTO;
import com.blue.foxbuy.models.DTOs.UserResultDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.JwtUtilService;
import com.blue.foxbuy.services.UserService;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@Tag(name = "Login")
public class LoginController {
    private final UserService userService;
    private final JwtUtilService jwtUtilService;

    @Autowired
    public LoginController(UserService userService, JwtUtilService jwtUtilService, UserRepository userRepository) {
        this.userService = userService;
        this.jwtUtilService = jwtUtilService;
    }

    @Operation(
            description = "Accepts form inputs (username and password) and authenticates the user.",
            summary = "Authenticates users.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "400",
                            description = "Returned when the username or password is blank.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"username\": \"The username cannot be blank.\"," +
                                            "\"password\": \"The password cannot be blank.\"," +
                                            "}")
                            )),
                    @ApiResponse(responseCode = "404",
                            description = "Returned when the username is not found in the database",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"404\"," +
                                            "\"message\": \"Username not found.\"," +
                                            "}")
                            )),
                    @ApiResponse(responseCode = "403",
                            description = "Returned when the user has not finished the e-mail verification process.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"403\"," +
                                            "\"message\": \"E-mail not verified, please check your e-mail inbox or spam folder, verify your e-mail and try logging in again.\"," +
                                            "}")
                            )),
                    @ApiResponse(responseCode = "401",
                            description = "Returned when the username and password don't match.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"401\"," +
                                            "\"message\": \"Provided username is already in use\"," +
                                            "}")
                            )),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the login is successful",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = JwtResponseDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"token\": \"eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlzcyI6IkZveGJ1eSIsInJvbGUiOiJBRE1JTiIsImlhdCI6MTcxMzk0NTY2MiwiZXhwIjoxNzEzOTQ5MjYyfQ.XgH6QqrVCyTqrzy-JkVJDtsPF_HPJmHo8fz3rifHuPo\"," +
                                            "}")
                            )),
                    @ApiResponse(responseCode = "403",
                            description = "Returned if the user has been banned.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"403\"," +
                                            "\"message\": \"Access denied. User has been banned until yyyy-MM-dd HH:mm:ss. Contact support for further assistance.\"," +
                                            "}")
                            ))
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody UserDTO userDTO) {
        if (userService.findByUsername(userDTO.getUsername()) == null) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("404");

            errorDTO.setMessage("Username not found.");

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorDTO);
        } else if (!userService.findByUsername(userDTO.getUsername()).isEmailVerified()) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("403");

            errorDTO.setMessage("E-mail not verified, please check your e-mail inbox or spam folder, verify your e-mail and try logging in again.");

            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
        } else if (userService.findByUsernameAndPassword(userDTO).isEmpty()) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("401");

            errorDTO.setMessage("Password or username incorrect.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
        }

        // Check whether the user has been banned
        User user = userService.findByUsername(userDTO.getUsername());

        if (user.isBanned()) {
            Date banDate = user.getBanDate();
            Date currentDate = new Date();

            if (currentDate.after(banDate)) {

                user.setBanned(false);

                user.setBanDate(null);

                userService.saveDirect(user);

                String jwtToken = jwtUtilService.generateJwtToken(userDTO.getUsername());

                return ResponseEntity.ok().body(new JwtResponseDTO(jwtToken));
            } else {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String banDateFormatted = dateFormat.format(banDate);
                ErrorDTO errorDTO = new ErrorDTO();

                errorDTO.setStatus("403");

                errorDTO.setMessage("Access denied. User has been banned until " + banDateFormatted + ". Contact support for further assistance.");

                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorDTO);
            }
        }

        String jwtToken = jwtUtilService.generateJwtToken(userDTO.getUsername());
        return ResponseEntity.ok().body(new JwtResponseDTO(jwtToken));
    }

    @GetMapping("/test")
    @Hidden
    public Authentication testing(Authentication authentication) {

        return authentication;
    }

    @GetMapping("/auth")
    @Hidden
    public ResponseEntity<?> userIdentityCheck(@RequestHeader(value = "authorization") String authenticationHeader) {
        Map<String, String> tokenDetails = jwtUtilService.parseToken(authenticationHeader);

        if (tokenDetails.get("valid").equals("false")) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("401");

            errorDTO.setMessage("The provided token is invalid.");

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorDTO);
        }

        String username = tokenDetails.get("username");
        String userID = userService.findByUsername(username).getId().toString();

        UserResultDTO userResultDTO = new UserResultDTO();

        userResultDTO.setUsername(username);

        userResultDTO.setId(userID);

        return ResponseEntity.ok(userResultDTO);
    }
}