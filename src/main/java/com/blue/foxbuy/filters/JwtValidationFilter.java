package com.blue.foxbuy.filters;

import com.blue.foxbuy.models.DTOs.ErrorDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class JwtValidationFilter extends OncePerRequestFilter {
    private final UserRepository userRepository;

    public JwtValidationFilter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException, DisabledException, BadCredentialsException {
        String authHeader = request.getHeader("Authorization");
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        }

        if (jwt != null) {
            SecretKey key = Keys.hmacShaKeyFor(System.getenv("JWT_KEY").getBytes(StandardCharsets.UTF_8));
            List<GrantedAuthority> authorities = new ArrayList<>();
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();

            // We extract the username from JWT claims
            String username = String.valueOf(claims.get("sub"));

            User user = userRepository.findByUsername(username);

            if (user == null) {

                throw new NullPointerException("Access denied. Invalid authentication token received. Please, log in and try again.");
            }

            // Then check if the user is banned
            if (user.isBanned()) {
                Date banDate = user.getBanDate();
                Date currentDate = new Date();

                // Then double check if the ban has expired, if
                // not throw an exception
                if (currentDate.before(banDate)) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String banDateFormatted = dateFormat.format(banDate);

                    throw new DisabledException("Access denied. This account has been banned until " + banDateFormatted + ". Please contact support for assistance.");
                } else {

                    // Remove ban if it has expired
                    user.setBanned(false);

                    user.setBanDate(null);

                    // Save updated user info
                    userRepository.save(user);
                }
            }

            // Authentication
            authorities.add(new SimpleGrantedAuthority(claims.get("role").toString()));

            Authentication auth = new UsernamePasswordAuthenticationToken(username, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        filterChain.doFilter(request, response);
    }
}