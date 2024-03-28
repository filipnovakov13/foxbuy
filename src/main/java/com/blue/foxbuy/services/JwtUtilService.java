package com.blue.foxbuy.services;

import com.blue.foxbuy.models.Role;

public interface JwtUtilService {

    String generateJwtToken(String username);
}
