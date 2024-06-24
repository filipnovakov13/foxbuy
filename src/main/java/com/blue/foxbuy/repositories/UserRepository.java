package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsernameIgnoreCase(String username);

    boolean existsByEmailIgnoreCase(String email);

    User findUserByEmailVerificationToken(String token);

    User findByUsername(String username);

    User findByUsernameAndPassword(String username, String password);
    long count();
}