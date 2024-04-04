package com.blue.foxbuy.config;

import com.blue.foxbuy.filters.JwtValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception{
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtValidationFilter(), BasicAuthenticationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                       .requestMatchers("/registration",
                                        "/login",
                                        "/verify-email",
                                        "/v3/api-docs",
                                        "/v3/api-docs/**",
                                        "/swagger-ui/index.html",
                                        "/swagger-ui/**").permitAll()
                        // .requestMatchers("/test").hasRole("ADMIN")
                       .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
            return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}