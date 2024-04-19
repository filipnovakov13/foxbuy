package com.blue.foxbuy.models.DTOs;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
@Schema(description = "Object sent to server for category manipulation")
public class AdCategoryDTO {

    @Schema(example = "IT")
    @NotBlank(message = "Name must be given to the category")
    private String name;
    @Schema(example = "Technical gizmos and gadgets")
    private String description;
}
