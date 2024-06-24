package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.UserService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImpTest {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
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
        assertFalse(userService.isEmailInUse("not@seznam.cz"));         // it is not
    }

    @Test
    void isSavedUserAdmin() throws MessagingException {
        userRepository.deleteAll();
        UserDTO userDTO = new UserDTO(
                "tom",
                "Password1!",
                "testing@testing.cz"
        );
        assertEquals(Role.ADMIN, userService.save(userDTO).getRole());
    }
}