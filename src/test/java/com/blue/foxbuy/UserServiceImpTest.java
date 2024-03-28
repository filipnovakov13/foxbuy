package com.blue.foxbuy;

import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImpTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        User user = new User(
                "shimmy",
                "Password1+-",
                "testing@seznam.cz",
                userService.emailVerificationStatus(),
                "emailToken",
                Role.USER
        );
        userRepository.save(user);
    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void isEmailValid() {
        assertFalse(userService.isEmailValid("testingidk.cz"));         // missing symbol @
        assertFalse(userService.isEmailValid("testing@idk.com."));      // ending with dot

        assertTrue(userService.isEmailValid("testing.testing@seznam.cz"));
        assertTrue(userService.isEmailValid("testing@idk.idk.idk"));
    }

    @Test
    void isPasswordValid() {
        assertFalse(userService.isPasswordValid("Oo1.567"));            // minimum 8 characters
        assertFalse(userService.isPasswordValid("oo1.5678"));           // missing uppercase
        assertFalse(userService.isPasswordValid("OO1.5678"));           // missing lowercase
        assertFalse(userService.isPasswordValid("Gay.Gay.Gay"));        // missing number
        assertFalse(userService.isPasswordValid("Gay1Gay2Gay3"));       // missing special character, "."

        assertTrue(userService.isPasswordValid("Password1!"));
    }

    @Test
    void isUsernameInUse() {
        assertTrue(userService.isUsernameInUse("shimmy"));              // it is
        assertTrue(userService.isUsernameInUse("Shimmy"));              // it is, ignoring case
        assertFalse(userService.isUsernameInUse("test"));               // it is not
    }

    @Test
    void isEmailInUse() {
        assertTrue(userService.isEmailInUse("testing@seznam.cz"));      // it is
        assertTrue(userService.isEmailInUse("Testing@seznam.cz"));      // it is, ignoring case
        assertFalse(userService.isEmailInUse("gay@seznam.cz"));         // it is not
    }

    @Test
    void isEmailVerificationOff(){
        assertFalse(userService.emailVerificationStatus());
    }
}