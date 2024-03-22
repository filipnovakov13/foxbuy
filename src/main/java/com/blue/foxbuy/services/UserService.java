package com.blue.foxbuy.services;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.User;
import jakarta.mail.MessagingException;

public interface UserService {

    boolean isEmailValid(String email);
    boolean isPasswordValid(String password);
    boolean isUsernameInUse(String username);
    boolean isEmailInUse(String email);
    User save(UserDTO userDTO) throws MessagingException;

    /*String encodedPassword(String password);*/

    boolean emailVerificationStatus();
}
