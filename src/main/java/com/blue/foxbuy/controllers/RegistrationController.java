package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.UserResultDTO;
import com.blue.foxbuy.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Tag(name = "Registration")
public class RegistrationController {

    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @Operation(
            description = "Takes user input, validates its correctness and creates a new user." +
                    " The first user is by default an Admin.",
            summary = "Responsible for creating new users.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "401",
                            description = "Returned when the email or password is not of the required format.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Invalid Format'}")
                            )
                    ),
                    @ApiResponse(responseCode = "409",
                            description = "Returned when the email or username is taken.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Username or email is already in use.'}")
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Returned when the registration email cannot be sent to the user.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'An error occurred while processing your request. Please try again later.'}")
                            )
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the user is created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResultDTO.class),
                                    examples = @ExampleObject(value = "{'id' : '434715c6-3672-4a8c-9feb-f804cc585829,'" +
                                            " 'username' : 'Adam'}")
                            )
                    )
            }
    )
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@RequestBody UserDTO userDTO) {
        try {
            if (!userService.isEmailValid(userDTO.getEmail()) || !userService.isPasswordValid(userDTO.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Invalid Format"));
            }
            if (userService.isUsernameInUse(userDTO.getUsername()) || userService.isEmailInUse(userDTO.getEmail())) {
                return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorDTO("Username or email is already in use"));
            }
            return ResponseEntity.ok().body(new UserResultDTO(userService.save(userDTO).getId().toString(), userDTO.getUsername()));
        } catch (MessagingException e) {
            System.out.println("Messaging error during registration");
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDTO("An error occurred while processing your request. Please try again later."));
        }
    }
}
