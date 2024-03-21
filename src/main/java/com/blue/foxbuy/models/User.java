package com.blue.foxbuy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data       // create getters and setters
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customer")
public class User {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    private UUID id;
    private String username;
    private String password;
    private String email;
    private boolean emailVerified;
    private String emailVerificationToken;

    // constructor without manual setting up ID
    public User(String username, String password, String email, boolean isEmailVerified, String emailVerificationToken) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.isEmailVerified = isEmailVerified;
        this.emailVerificationToken = emailVerificationToken;
    }
}
