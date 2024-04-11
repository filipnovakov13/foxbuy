package com.blue.foxbuy.models.DTOs;

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
    private String banned_until;

    public BanResultDTO (String username, Date banDuration) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.username = username;
        this.banned_until = dateFormat.format(banDuration);
    }
}
