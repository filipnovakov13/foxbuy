package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.DTOs.AdResultDTO;
import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.DTOs.UserDetailsDTO;
import com.blue.foxbuy.models.Role;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.EmailService;
import com.blue.foxbuy.services.TokenGenerationService;
import com.blue.foxbuy.services.UserService;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerationService tokenGenerationService;

    private final AdServiceImp adServiceImp;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, TokenGenerationService tokenGenerationService, AdServiceImp adServiceImp) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerationService = tokenGenerationService;
        this.adServiceImp = adServiceImp;
    }

    @Override
    public boolean isUsernameInUse(String username) {
        return userRepository.existsByUsernameIgnoreCase(username);
    }

    @Override
    public boolean isEmailInUse(String email) {
        return userRepository.existsByEmailIgnoreCase(email);
    }

    @Override
    public User save(UserDTO userDTO) throws MessagingException {
        User user = new User(
                userDTO.getUsername(),
                encodedPassword(userDTO.getPassword()),
                userDTO.getEmail(),
                emailVerificationStatus(),
                tokenGenerationService.tokenGeneration(),
                Role.USER
        );

        if (userRepository.count() <= 0) {
            user.setRole(Role.ADMIN);
        }

        if (!emailVerificationStatus()) {
            emailService.sendEmailVerification(user.getEmail(), "Foxbuy e-mail verification", user.getEmailVerificationToken(), user.getUsername());
        }
        return userRepository.save(user);
    }

    @Override
    public void saveDirect(User user) {
        userRepository.save(user);
    }

    @Override
    public String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean emailVerificationStatus() {
        if (System.getenv().get("verification") != null) {              // if it's not empty -> it's turn on
            return System.getenv().get("verification").equals("false"); // if it's false -> it's turn off, user should get true
        } else {
            return true;
        }
    }

    @Override
    public User findByUsername(String username) throws NullPointerException {

        try {

            return userRepository.findByUsername(username);
        } catch (NullPointerException e) {

            throw new NullPointerException("User not found.");
        }
    }

    @Override
    public Optional<User> findByUsernameAndPassword(UserDTO userDTO) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userDTO.getUsername()));

        if (user.isPresent() && passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }

    @Override
    public Optional<UserDetailsDTO> findById(UUID id) {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()) {
            List<AdResultDTO> userAds = adServiceImp.getAdResultsByUser(user.get().getUsername());
            UserDetailsDTO userDetails = new UserDetailsDTO(user.get().getUsername(),
                                                            user.get().getEmail(),
                                                            user.get().getRole(),
                                                            userAds);
            return Optional.of(userDetails);
        }
        return Optional.empty();
    }
}