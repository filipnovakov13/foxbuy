package com.blue.foxbuy.models.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BanDTO {

    @NotNull(message = "Please provide a duration of days the user will be banned")
    @Positive(message = "Ban duration value must be positive")
    private Integer duration;
}
