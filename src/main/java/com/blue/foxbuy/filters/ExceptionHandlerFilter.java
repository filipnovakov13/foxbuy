package com.blue.foxbuy.filters;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.services.ConversionService;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.DisabledException;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

// Filter and Spring Security exception handler
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    private final ConversionService conversionService;

    public ExceptionHandlerFilter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {

            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("401");

            errorDTO.setMessage("Access denied. Your security token has expired. Please, log in again.");

            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            response.getWriter().write(conversionService.convertObjectToJson(errorDTO));
        } catch (DisabledException e) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("401");

            errorDTO.setMessage(e.getMessage());

            response.setStatus(HttpStatus.FORBIDDEN.value());

            response.getWriter().write(conversionService.convertObjectToJson(errorDTO));
        } catch (NullPointerException e) {
            ErrorDTO errorDTO = new ErrorDTO();

            errorDTO.setStatus("401");

            errorDTO.setMessage(e.getMessage());

            response.setStatus(HttpStatus.UNAUTHORIZED.value());

            response.getWriter().write(conversionService.convertObjectToJson(errorDTO));
        }
    }
}