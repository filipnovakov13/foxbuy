package com.blue.foxbuy.services.implementations;

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
        User user = new User(
                "john",
                "Password1!",
                "john@example.com",
                true,
                "emailToken");
        userRepository.save(user);
    }

    @Test
    public void parseTokenTest_validAuthenticationHeader_returnValidTrue() {
        String authHeader = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huIiwiaXNzIjoiRm94YnV5IiwiaWF0IjoxNzExNTQ4ODk0LCJleHAiOjE3MTE1NTI0OTR9.7DxgoiTU_c9p8fKhAGh9dVBo3XEmR4P5AIcHGAi-Q0k";
        Map<String, String> jwtData = jwtUtilService.parseToken(authHeader);
        assertTrue(jwtData.get("valid").equals("true"));
    }

    @Test
    public void parseTokenTest_validAuthenticationHeader_returnValidFalse() {
        String authHeader = "wrongToken";
        Map<String, String> jwtData = jwtUtilService.parseToken(authHeader);
        assertTrue(jwtData.get("valid").equals("false"));
    }
}