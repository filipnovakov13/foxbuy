package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.services.ConversionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

@Service
public class ConversionServiceImpl implements ConversionService {

    @Override
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object != null) {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(object);
        } else return null;
    }
}
