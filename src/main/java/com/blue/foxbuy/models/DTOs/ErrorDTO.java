package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "An object used to send back verbose error messages to the user.")
public class ErrorDTO {

    @Schema(description = "An HTTP response status naming the nature of the error.",
            example = "401")
    private String status;

    @Schema(description = "The actual error message associated with the status.",
            example = "Access denied. User has been banned.")
    private String message;
}