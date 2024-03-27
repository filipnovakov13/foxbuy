package com.blue.foxbuy.models.DTOs;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JwtResponseDTO {

    private String token;

    public JwtResponseDTO(String token) {
        this.token = token;
    }

}
