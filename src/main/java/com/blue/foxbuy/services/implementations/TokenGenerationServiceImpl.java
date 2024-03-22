package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.services.TokenGenerationService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class TokenGenerationServiceImpl implements TokenGenerationService {
    @Override
    public String tokenGeneration() {
        return UUID.randomUUID().toString();
    }
}