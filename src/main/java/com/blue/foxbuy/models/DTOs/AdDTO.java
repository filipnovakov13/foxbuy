package com.blue.foxbuy.models.DTOs;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {

    @Valid

    @NotBlank(message = "Add a title")
    @Size(min = 5, max = 200, message = "The description must be between 5 and 200 characters long")
    private String title;

    @NotBlank(message = "Write out a description")
    @Size(min = 5, max = 3189, message = "The description must be between 5 and 3189 characters in length")
    private String description;

    @NotNull(message = "Include a price")
    @PositiveOrZero(message = "Price must be positive or zero")
    @Digits(integer = 12, fraction = 2, message = "Price can have up to 12 digits before and 2 digits after the decimal point")
    private Double price;

    @NotBlank(message = "Set your zipcode")
    @Pattern(regexp = "\\d{5}", message = "ZIP code must be exactly 5 digits")
    private String zipcode;

    @NotNull(message = "Choose a category")
    private Integer categoryID;
}