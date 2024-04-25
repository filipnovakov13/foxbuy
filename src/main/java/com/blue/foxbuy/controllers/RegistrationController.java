package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.UserResultDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


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
            summary = "Registers new users.",
            method = "POST",
            responses = {
                    @ApiResponse(responseCode = "400",
                            description = "Returned when the e-mail or password is not of the required format.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"400\"," +
                                            "\"username\": \"The username must be between 2 and 50 characters long.\"," +
                                            "\"password\": \"The password must be a minimum of 8 characters long and must include 1 uppercase, 1 lowercase letter, 1 special character and 1 number.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "409",
                            description = "Returned when the username or e-mail is taken.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = HashMap.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"409\"," +
                                            "\"username\": \"Provided username is already in use\"," +
                                            "\"email\": \"An error occurred while processing your request. Try again later and if the error persists, contact support for further assistance.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "500",
                            description = "Returned when the registration email cannot be sent to the user.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"status\": \"500\"," +
                                            "\"message\": \"An error occurred while processing your request. Try again later and if the error persists, contact support for further assistance.\"" +
                                            "}")
                            )
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the user is created.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = UserResultDTO.class),
                                    examples = @ExampleObject(value = "{" +
                                            "\"id\": \"2dd1090e-9d8c-4d3c-8123-fc6e82f4b4d0\"," +
                                            "\"username\": \"Adam\"" +
                                            "}")
                            )
                    )
            }
    )
    @PostMapping("/registration")
    public ResponseEntity<?> registration(@Valid @RequestBody UserDTO userDTO) throws MessagingException {
        Map<String, String> errors = new HashMap<>();

        if (userService.isUsernameInUse(userDTO.getUsername())) {

            errors.put("username", "This username is already in use.");
        }

        if (userService.isEmailInUse(userDTO.getEmail())) {

            errors.put("email", "This e-mail is already in use.");
        }

        if (!errors.isEmpty()) {

            return ResponseEntity.badRequest().body(errors);
        }

        try {

            User user = userService.save(userDTO);

            return ResponseEntity.ok().body(new UserResultDTO(user));
        } catch (MessagingException e) {
            throw new MessagingException("An error occurred while processing your request. Try again later and if the error persists, contact support for further assistance.");
        }
    }
}
