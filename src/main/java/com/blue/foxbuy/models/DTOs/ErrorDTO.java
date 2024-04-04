package com.blue.foxbuy.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDTO {

    private String error;
}
