package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.JwtUtilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
class JwtUtilServiceImplTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    JwtUtilService jwtUtilService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void parseTokenTest_validAuthenticationHeader_returnTrue() {
        User user = new User(
                "john",
                "Password1!",
                "john@example.com",
                true,
                "emailToken",
                Role.USER);
        userRepository.save(user);

        String authHeader = jwtUtilService.generateJwtToken(user.getUsername());
        Map<String, String> jwtData = jwtUtilService.parseToken(authHeader);
        assertTrue(jwtData.get("valid").equals("true"));
    }

    @Test
    public void parseTokenTest_validAuthenticationHeader_returnFalse() {
        String authHeader = "wrongToken";
        Map<String, String> jwtData = jwtUtilService.parseToken(authHeader);
        assertTrue(jwtData.get("valid").equals("false"));
    }
}