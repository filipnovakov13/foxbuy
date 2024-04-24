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
import java.util.HashMap;
import java.util.Map;

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

    @Override
    public Map<String, String> parseToken(String authenticationHeader) {

        // Variable declarations and initializations
        // We create a claims object to hold the decrypted contents of the JWT,
        // a jwtData map to hold our response to the controller and an actual
        // String for the JWT that we are going to extract
        Claims claims;
        Map<String, String> jwtData = new HashMap<>();
        String jwt = null;

        // JWT extraction
        if (authenticationHeader != null && authenticationHeader.startsWith("Bearer ")) {
            jwt = authenticationHeader.substring(7);
        } else jwt = authenticationHeader;

        // JWT exception handling
        // Here we check for a missing or tampered secret key and decipher
        // the JWT that the user tried accessing the controller with.
        // If we try to write some garbled mess into the claims variable
        // we will know that his secret key was incorrect and JVM will
        // throw an exception we will catch that and send the jwtData map
        // with the sole key of "valid" as "false" to the controller

        try {
            claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
        } catch (Exception e) {
            jwtData.put("valid", "false");
            return jwtData;
        }

        // Otherwise if the above parsing goes smoothly we will populate
        // the map with the username and a valid key that says "true"
        // and send that back

        jwtData.put("valid", "true");
        jwtData.put("username", claims.get("sub").toString());

        return jwtData;
    }
}
