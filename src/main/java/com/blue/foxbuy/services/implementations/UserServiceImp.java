package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.DTOs.UserDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import com.blue.foxbuy.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class UserServiceImp implements UserService {

    // dependencies
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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
    public User save(UserDTO userDTO) {
        User user = new User(
                userDTO.getEmail(),
                userDTO.getUsername(),
                (encodedPassword(userDTO.getPassword())),
                isEmailVerificationOn(),
                UUID.randomUUID().toString()
        );
        return userRepository.save(user);
    }

    @Override
    public String encodedPassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public boolean isEmailVerificationOn() {
        return System.getenv().containsKey("verification");
    }
}
