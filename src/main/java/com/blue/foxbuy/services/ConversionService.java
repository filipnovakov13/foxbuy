package com.blue.foxbuy.services;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface ConversionService {
    String convertObjectToJson(Object object) throws JsonProcessingException;
}
