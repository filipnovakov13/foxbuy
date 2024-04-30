package com.blue.foxbuy.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Foxbuy API specification",
                description = "OpenAPI documentation for the Foxbuy application",
                version = "1.0-pre_alpha4"
        ),
        servers = {
                @Server(
                        description = "Local Development Environment",
                        url = "http://localhost:8080"
                )
        }
)
@SecurityScheme(
        name = "BearerToken",
        description = "Provide a valid JWT",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {
}