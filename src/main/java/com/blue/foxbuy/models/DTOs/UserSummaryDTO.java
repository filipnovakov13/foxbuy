package com.blue.foxbuy.models.DTOs;

import com.blue.foxbuy.models.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Returns single user's details with just a count of ads connected with that user")
public class UserSummaryDTO {

    @Schema(example = "Adam")
    private String username;
    @Schema(example = "adamoldboy@gmail.com")
    private String email;
    @Schema(example = "VIP")
    private Role role;
    @Schema(example = "10")
    private int adCount;
}
