package com.blue.foxbuy.models.DTOs;

import com.blue.foxbuy.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Returns single user's details and the list of ads connected with that user")
public class UserDetailsDTO {

    @Schema(example = "Adam")
    private String username;
    @Schema(example = "adamoldboy@gmail.com")
    private String email;
    @Schema(example = "VIP")
    private Role role;
    @Schema(example = """
            {
                {
                    “id”: 10,
                    “title”: “Leviathan Axe”,
                    “description”: “Good axe to kill norse gods. Used, some scratches and blood marks.”,
                    “price”: 3000.00,
                    “zipcode”: 12345,
                    “categoryID”: 3
                },
                {
                    “id”: 11,
                    “title”: “Something important”,
                    “description”: “You’ll figure it out”,
                    “price”: 20.00,
                    “zipcode”: 99999,
                    “categoryID”: 1
                }
            }
            """)
    private List<AdResultDTO> ads;
}
