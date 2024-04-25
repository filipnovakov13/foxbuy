package com.blue.foxbuy.filters;

import com.blue.foxbuy.models.Log;
import com.blue.foxbuy.repositories.LogRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class LoggingFilter extends OncePerRequestFilter {
    private final LogRepository logRepository;

    public LoggingFilter(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(requestWrapper, responseWrapper);
        String requestBody = getStringValue(requestWrapper.getContentAsByteArray(), request.getCharacterEncoding());

        Log requestLog = new Log(request.getMethod() + " " + request.getRequestURI(), "INFO", requestBody);
        logRepository.save(requestLog);

        responseWrapper.copyBodyToResponse();
    }

    private String getStringValue(byte[] requestBodyAsByteArray, String characterEncoding) {

        try {

            return new String(requestBodyAsByteArray, 0, requestBodyAsByteArray.length, characterEncoding);
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }

        return "";
    }
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return true;
    }

    @Override
    protected boolean shouldNotFilterErrorDispatch() {
        return true;
    }
}