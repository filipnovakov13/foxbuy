package com.blue.foxbuy.config;

import com.blue.foxbuy.filters.ExceptionHandlerFilter;
import com.blue.foxbuy.filters.JwtValidationFilter;
import com.blue.foxbuy.filters.LoggingFilter;
import com.blue.foxbuy.repositories.LogRepository;
import com.blue.foxbuy.services.ConversionService;
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
    private final ConversionService conversionService;
    private final UserRepository userRepository;
    private final LogRepository logRepository;

    @Autowired
    public SecurityConfig(ConversionService conversionService, UserRepository userRepository, LogRepository logRepository) {
        this.conversionService = conversionService;
        this.userRepository = userRepository;
        this.logRepository = logRepository;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtValidationFilter(userRepository), BasicAuthenticationFilter.class)
                .addFilterBefore(new ExceptionHandlerFilter(conversionService), JwtValidationFilter.class)
                .addFilterAfter(new LoggingFilter(logRepository), JwtValidationFilter.class)
                .authorizeHttpRequests((requests) -> requests
                        // Endpoints accessible by everyone
                        .requestMatchers("/registration",
                                "/login",
                                "/verify-email",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/index.html",
                                "/swagger-ui/**",
                                "/category")
                        .permitAll()

                        // Endpoints accessible by admins
                        .requestMatchers("/test",
                                "/user/*/ban",
                                "/logs",
                                "/category",
                                "/category/**")
                        .hasAuthority("ADMIN")

                        // Specific access endpoints
                        .requestMatchers("/advertisement", "/advertisement/**").hasAnyAuthority("ADMIN", "VIP_USER", "USER")
                        .requestMatchers(HttpMethod.POST, "/category").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/category/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/category/**").hasAuthority("ADMIN")
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}