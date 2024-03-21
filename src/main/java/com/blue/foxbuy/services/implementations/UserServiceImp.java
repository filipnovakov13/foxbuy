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

    @Autowired
    public UserServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
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
    public User save(UserDTO userDTO) {                     // include password encoder Spring Security?
        User user = new User(
                userDTO.getEmail(),
                userDTO.getUsername(),
                (hashPassword(userDTO.getPassword())),      // JCA, JCE, AES or Java KeyStore -> use BCrypt
                isEmailVerificationOff(),
                UUID.randomUUID().toString()
        );
        return userRepository.save(user);
    }

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    public boolean isPasswordCorrect(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }

    @Override
    public boolean isEmailVerificationOff() {
        // check if the functionality is on, then set it to false
        // if not - set it to true
        return true;
    }
}
