package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.EmailService;
import com.blue.foxbuy.services.TokenGenerationService;
import com.blue.foxbuy.services.UserService;

import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class UserServiceImp implements UserService {

    // dependencies
    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenGenerationService tokenGenerationService;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder, EmailService emailService, PasswordEncoder passwordEncoder1, TokenGenerationService tokenGenerationService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.tokenGenerationService = tokenGenerationService;
    }

    // methods
    @Override
    public boolean isEmailValid(String email) {
        String email_regex = "^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@"
                + "[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(email_regex);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    @Override
    public boolean isPasswordValid(String password) {   // https://www.geeksforgeeks.org/how-to-validate-a-password-using-regular-expressions-in-java/
        String password_regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&!+=()]).{8,20}$";
        Pattern pattern = Pattern.compile(password_regex);
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
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
                tokenGenerationService.tokenGeneration()
        );

        if (!emailVerificationStatus()) {
            emailService.sendEmailVerification(user.getEmail(), "Foxbuy e-mail verification", user.getEmailVerificationToken(), user.getUsername());
        }
        return userRepository.save(user);
    }

    @Override
    public String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean emailVerificationStatus() {
        if (System.getenv().get("verification") != null){               // if it's not empty -> it's turn on
            return System.getenv().get("verification").equals("false"); // if it's false -> it's turn off, user should get true
        }
        return false;
    }

    @Override
    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByUsernameAndPassword(UserDTO userDTO) {
        Optional<User> user = Optional.ofNullable(userRepository.findByUsername(userDTO.getUsername()));
        if (user.isPresent() && passwordEncoder.matches(userDTO.getPassword(), user.get().getPassword())) {
            return user;
        }
        return Optional.empty();
    }
}
