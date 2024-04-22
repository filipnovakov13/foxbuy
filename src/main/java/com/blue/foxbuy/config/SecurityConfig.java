package com.blue.foxbuy.config;

import com.blue.foxbuy.filters.JwtValidationFilter;
import com.blue.foxbuy.filters.LoggingFilter;
import com.blue.foxbuy.repositories.LogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import com.blue.foxbuy.repositories.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final UserRepository userRepository;
    private final LogRepository logRepository;
    @Autowired
    public SecurityConfig(UserRepository userRepository, LogRepository logRepository) {
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                // .addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class)
                .addFilterAfter(new LoggingFilter(logRepository), JwtValidationFilter.class)
                .authorizeHttpRequests(requests -> requests
                       .requestMatchers("/registration",
                                        "/login",
                                        "/verify-email",
                                        "/advertisement",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/index.html",
                                        "/swagger-ui/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/category").permitAll()
                        // .requestMatchers("/test").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/category").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/logs").hasAuthority("ADMIN")
                       .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}