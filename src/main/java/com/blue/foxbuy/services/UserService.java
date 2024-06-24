package com.blue.foxbuy.services;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.UserDetailsDTO;
import com.blue.foxbuy.models.User;
import jakarta.mail.MessagingException;

import java.util.Optional;
import java.util.UUID;

public interface UserService {

    boolean isUsernameInUse(String username);

    boolean isEmailInUse(String email);

    User save(UserDTO userDTO) throws MessagingException;

    void saveDirect(User user);

    String encodedPassword(String password);

    boolean emailVerificationStatus();

    User findByUsername(String username);

    Optional<User> findByUsernameAndPassword(UserDTO userDTO);

    Optional<UserDetailsDTO> findById(UUID id);
}
