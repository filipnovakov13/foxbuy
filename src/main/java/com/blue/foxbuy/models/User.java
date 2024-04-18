package com.blue.foxbuy.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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
    private Role role;
    private boolean banned;
    private Date banDate;
    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Ad> ads;


    // constructor without manual setting up ID
    public User(String username, String password, String email, boolean emailVerified, String emailVerificationToken, Role role) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.emailVerified = emailVerified;
        this.emailVerificationToken = emailVerificationToken;
        this.role = role;
        this.banned = false;
        this.banDate = null;
        this.ads = new ArrayList<>();
    }
}
