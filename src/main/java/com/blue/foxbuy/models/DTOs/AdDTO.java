package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "An object used to accept properties required for creating new advertisements and listings and transmitting such data back and forth between the user and the server.")
public class AdDTO {


    @Schema(description = "The title must be between 5 and 200 characters long",
            example = "iPhone 15 Pro Max 1TB")
    @NotBlank(message = "Add a title")
    @Size(min = 5, max = 200, message = "The title must be between 5 and 200 characters long")
    private String title;

    @Schema(description = "The description must be between 5 and 3189 characters in length",
            example = "I'm selling my 2 months old, barely used iPhone 15 Pro Max with 1TB of storage. It's in great condition. Original box and clear Magsafe cover included.")
    @NotBlank(message = "Write out a description")
    @Size(min = 5, max = 3189, message = "The description must be between 5 and 3189 characters in length")
    private String description;

    @Schema(description = "The price can have up to 12 digits before and 2 digits after a decimal point",
            example = "41000")
    @NotNull(message = "Include a price")
    @PositiveOrZero(message = "Price must be positive or zero")
    @Digits(integer = 12, fraction = 2, message = "The price can have up to 12 digits before and 2 digits after a decimal point")
    private Double price;

    @Schema(description = "The ZIP code must be exactly 5 digits",
            example = "12345")
    @NotBlank(message = "Set your zipcode")
    @Pattern(regexp = "\\d{5}", message = "The ZIP code must be exactly 5 digits")
    private String zipcode;

    @Schema(description = "The category is chosen from a drop-down list and is then sent as an integer within this DTO",
            example = "1")
    @NotNull(message = "Choose a category")
    private Integer categoryID;
}