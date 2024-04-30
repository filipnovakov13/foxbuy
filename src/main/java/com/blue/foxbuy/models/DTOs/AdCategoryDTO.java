package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "An object used to accept properties required for creating new ad categories and transmitting such data back and forth between the user and the server.")
public class AdCategoryDTO {

    @Schema(description = "Do not fill in, unless you want an error. This is generated automatically and only appears in responses after the ad category is created.",
            example = "1")
    private Integer id;

    @Schema(example = "IT")
    @NotBlank(message = "The name must not be blank")
    private String name;

    @Schema(example = "Information technology gizmos and gadgets")
    @NotBlank(message = "The description must not be blank.")
    private String description;

    public AdCategoryDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
