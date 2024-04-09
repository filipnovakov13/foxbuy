package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Object used to accept properties required for creating new users")
public class UserDTO {
    @Schema(example = "Adam")
    private String username;
    @Schema(description = "Minimum 8 characters long. " +
            "Must include 1 uppercase, 1 lowercase letter, 1 special character and 1 number",
            example = "Anything1!")
    private String password;
    @Schema(example = "adam@gmail.com")
    private String email;
}
