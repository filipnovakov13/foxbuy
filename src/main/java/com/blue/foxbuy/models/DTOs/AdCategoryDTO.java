package com.blue.foxbuy.models.DTOs;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Schema(description = "Object sent to server for category manipulation")
public class AdCategoryDTO {

    @NotEmpty(message = "Name must be given to the category")
    @Schema(example = "IT")
    private String name;
    @Schema(example = "Technical gizmos and gadgets")
    private String description;
}
