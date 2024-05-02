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
                .authorizeHttpRequests(requests -> requests
                        // Endpoints accessible by everyone
                        .requestMatchers("/registration",
                                "/login",
                                "/verify-email",
                                "/v3/api-docs",
                                "/v3/api-docs/**",
                                "/swagger-ui/index.html",
                                "/swagger-ui/**")
                        .permitAll()

                        .requestMatchers(HttpMethod.GET, "/advertisement", "/advertisement/**", "/category",
                                "/category/**")
                        .permitAll()

                        // Endpoints accessible by admins
                        .requestMatchers("/test",
                                "/user/*/ban",
                                "/logs",
                                "/auth")
                        .hasAuthority("ADMIN")

                        // Post, Put, Delete endpoints accessible by admins
                        .requestMatchers(HttpMethod.POST, "/category",
                                "/category/**")
                        .hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.PUT, "/category",
                                "/category/**")
                        .hasAuthority("ADMIN")

                        .requestMatchers(HttpMethod.DELETE, "/category",
                                "/category/**")
                        .hasAuthority("ADMIN")

                        // Post, Put, Delete endpoints accessible by admins and registered users
                        .requestMatchers(HttpMethod.POST, "/advertisement",
                                "/advertisement/**")
                        .hasAnyAuthority("ADMIN", "VIP_USER", "USER")

                        .requestMatchers(HttpMethod.PUT, "/advertisement",
                                "/advertisement/**")
                        .hasAnyAuthority("ADMIN", "VIP_USER", "USER")

                        .requestMatchers(HttpMethod.DELETE, "/advertisement",
                                "/advertisement/**")
                        .hasAnyAuthority("ADMIN", "VIP_USER", "USER")

                        .anyRequest()
                        .authenticated())

                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}