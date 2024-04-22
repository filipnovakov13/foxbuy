package com.blue.foxbuy.models.DTOs;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BanResultDTO {
    private String username;
    @JsonProperty("banned_until")
    private String bannedUntil;

    public BanResultDTO (String username, Date banDuration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.username = username;
        this.bannedUntil = dateFormat.format(banDuration);
    }
}
