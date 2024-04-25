package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.services.ConversionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ConversionServiceImpl implements ConversionService {
    private ObjectMapper mapper;

    @Override
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object != null) {
            mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } else return null;
    }

    @Override
    public JsonNode readTree(String string) throws JsonProcessingException {
        if (string != null) {
            mapper = new ObjectMapper();
            return mapper.readTree(string);
        } else return null;
    }
}
