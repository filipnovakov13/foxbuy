package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "An object used to accept properties required for creating new users and transmitting user data back and forth between the user and the server.")
public class UserDTO {
    @Schema(example = "Adam")
    @NotBlank(message = "The username cannot be blank.")
    @Size(min = 2, max = 50, message = "The name must be between 2 and 50 characters long.")
    private String username;

    @Schema(description = "The password must be a minimum of 8 characters long and must include 1 uppercase, 1 lowercase letter, 1 special character and 1 number.",
            example = "Anything1!")
    @NotBlank(message = "The password cannot be blank.")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=()]).{8,20}$",
            message = "The password must be a minimum of 8 characters long and must include 1 uppercase, 1 lowercase letter, 1 special character and 1 number.")
    private String password;

    @Schema(example = "adam@gmail.com")
    @Pattern(regexp = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
            + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$", message = "Please, provide a valid e-mail address.")
    private String email;
}
