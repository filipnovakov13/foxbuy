package com.blue.foxbuy.handler;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // Validation exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException e) throws JsonProcessingException {

        Map<String, String> errors = new HashMap<>();

        errors.put("status", "400");

        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }

    // Json processing exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(JsonProcessingException.class)
    public ResponseEntity<ErrorDTO> handleJsonProcessingExceptions(JsonProcessingException e) throws JsonProcessingException {
        ErrorDTO errorDTO = new ErrorDTO();
        errorDTO.setStatus("400");

        errorDTO.setMessage("Oops. An error has occurred while processing your request. Please, try again or contact support for assistance.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    // Null pointer exception handler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    public ResponseEntity<ErrorDTO> handleNullPointerExceptions(NullPointerException e) throws JsonProcessingException {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatus("400");

        errorDTO.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    // Messaging exception handler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MessagingException.class)
    public ResponseEntity<ErrorDTO> handleMessagingExceptions(MessagingException e) throws JsonProcessingException {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatus("500");

        errorDTO.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
    }

    // Incorrect date format handler
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorDTO> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException e) {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatus("400");

        errorDTO.setMessage("Invalid argument format. Fix the format of the parameter in the URL and try again.");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }

    // Incorrect parameter handler
    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDTO> handleNumberFormatException(NumberFormatException e) {
        ErrorDTO errorDTO = new ErrorDTO();

        errorDTO.setStatus("400");

        errorDTO.setMessage(e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
    }
}
