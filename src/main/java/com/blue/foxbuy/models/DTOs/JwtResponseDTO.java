package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Schema(description = "An object used to send back JWTs to the user after a successful login.")
public class JwtResponseDTO {

    @Schema(example = "eyJhbGciOiJIUzI1NiJ9." +
            "eyJzdWIiOiJBZGFtMDIxIiwiaXNzIjo" +
            "iRm94YnV5Iiwicm9sZSI6IkFETUlOIi" +
            "wiaWF0IjoxNzEyNTc4NjIyLCJleHAiOjE3MTI1ODIyMjJ9." +
            "jDexkYDlBacOFWW_0Jf4ofVcd1QJXKxMhWVqx0zjBbQ")
    private String token;

    public JwtResponseDTO(String token) {
        this.token = token;
    }

}
