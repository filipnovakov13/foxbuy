package com.blue.foxbuy.services;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.User;

public interface UserService {

    boolean isEmailValid(String email);
    boolean isPasswordValid(String password);
    boolean isUsernameInUse(String username);
    boolean isEmailInUse(String email);
    User save(UserDTO userDTO);

    String hashPassword(String password);
    boolean isPasswordCorrect(String password, String hashedPassword);

    boolean isEmailVerificationOff();
}
