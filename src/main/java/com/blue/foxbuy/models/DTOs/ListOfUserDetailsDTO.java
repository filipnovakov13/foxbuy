package com.blue.foxbuy.models.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Returns all the users details")
public class ListOfUserDetailsDTO {
    @Schema(example = "1")
    private int page;
    @Schema(example = "5")
    private int totalPages;
    @Schema(example = """
            {
               "username":"user123",
                "email":"email@email.com",
                "role":"VIP",
                "ads":10
            },
            {
                "username":"user222",
                "email":"email2@email.com",
                "role":"USER",
                "ads":1
            }
            """)
    private List<UserSummaryDTO> users;
}
