package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Object returned upon successful user registration")
public class UserResultDTO {

    @Schema(description = "Id given to the created user in the database",
            example = "434715c6-3672-4a8c-9feb-f804cc585829")
    private String id;
    @Schema(example = "Adam")
    private String username;
}
