package com.blue.foxbuy.services;

import java.util.Map;
import com.blue.foxbuy.models.Role;

public interface JwtUtilService {

    String generateJwtToken(String username);

    Map<String, String> parseToken(String jwt);
}
