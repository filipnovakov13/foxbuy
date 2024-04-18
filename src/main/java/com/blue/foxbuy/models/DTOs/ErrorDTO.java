package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Object used to send back error messages to the user")
public class ErrorDTO {

    @Schema(example = "Username/Ad/Category not found")
    private String error;
}
