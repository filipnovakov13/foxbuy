package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.services.ConversionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private final ConversionService conversionService;

    public GlobalExceptionHandler(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    // Validation exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationExceptions(MethodArgumentNotValidException e) throws JsonProcessingException {

        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(conversionService.convertObjectToJson(errors)));
    }

    // Json processing exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorDTO> handleJsonProcessingExceptions(JsonProcessingException e) throws JsonProcessingException {
        Map<String, String> errors = new HashMap<>();
        errors.put("status", "400");
        errors.put("message", "Oops. An error has occurred while processing your request. Please, try again or contact support for assistance.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(conversionService.convertObjectToJson(errors)));
    }
}
