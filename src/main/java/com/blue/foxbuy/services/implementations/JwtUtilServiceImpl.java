package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.User;
import com.blue.foxbuy.services.JwtUtilService;
import com.blue.foxbuy.services.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtUtilServiceImpl implements JwtUtilService {

    private final UserService userService;
    SecretKey key = Keys.hmacShaKeyFor(System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8));

    public JwtUtilServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String generateJwtToken(String username) {
        User user = userService.findByUsername(username);

        Claims claims = Jwts.claims()
                .setSubject(username)
                .setIssuer("Foxbuy");

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + 3600000);

        return Jwts.builder()
                .setClaims(claims)
                .claim("role", user.getRole())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(Keys.hmacShaKeyFor(key.getEncoded()))
                .compact();
    }
}
