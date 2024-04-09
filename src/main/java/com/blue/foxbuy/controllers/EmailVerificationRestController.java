package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;

import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "E-mail verification")
public class EmailVerificationRestController {
    private final UserRepository userRepository;

    @Autowired
    public EmailVerificationRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(
            description = "Accepts a token as a url parameter," +
                    " if a user has verified his email address updates this information in the database.",
            summary = "Endpoint for setting the user email verification status",
            method = "GET",
            responses = {
                    @ApiResponse(responseCode = "401",
                            description = "Returned when the token used is invalid",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = ErrorDTO.class),
                                    examples = @ExampleObject(value = "{'error' : 'Invalid verification token'}")
                            )
                    ),
                    @ApiResponse(responseCode = "200",
                            description = "Returned when the user's verification has been updated.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(),
                                    examples = @ExampleObject(value = "{'E-mail verified successfully'}")
                            )
                    )
            }
    )
    @GetMapping("/verify-email")
    public ResponseEntity<?> verifyEmail(@Parameter (name = "token", description = "Token used to verify users email.", example = "513d7d48-579a-49d3-9442-0241788311a3")
                                                  @RequestParam("token") String token) {
        User user = userRepository.findUserByEmailVerificationToken(token);

        if (user != null) {
            user.setEmailVerified(true);
            userRepository.save(user);
            return ResponseEntity.ok("E-mail verified successfully");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDTO("Invalid verification token"));
        }
    }
}