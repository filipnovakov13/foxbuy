package com.blue.foxbuy.models.DTOs;

import com.blue.foxbuy.models.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "An object that is returned upon successful user registration.")
public class UserResultDTO {

    @Schema(description = "Id given to the created user in the database",
            example = "434715c6-3672-4a8c-9feb-f804cc585829")
    private String id;

    @Schema(example = "Adam")
    private String username;

    @Schema(example = "E-mail verified successfully.")
    private String message;

    public UserResultDTO(User user) {
        this.username = user.getUsername();
        this.id = user.getId().toString();
    }
}
