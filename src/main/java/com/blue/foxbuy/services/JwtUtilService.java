package com.blue.foxbuy.services;

import java.util.Map;

public interface JwtUtilService {

    String generateJwtToken(String username);

    Map<String, String> parseToken(String jwt);
}
