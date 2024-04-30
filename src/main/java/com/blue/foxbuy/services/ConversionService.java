package com.blue.foxbuy.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;

public interface ConversionService {
    String convertObjectToJson(Object object) throws JsonProcessingException;

    JsonNode readTree(String string) throws JsonProcessingException;
}
