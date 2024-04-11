package com.blue.foxbuy.services;

import com.blue.foxbuy.models.DTOs.BanDTO;
import com.blue.foxbuy.models.DTOs.BanResultDTO;

import java.util.UUID;

public interface AdminService {
    BanResultDTO banUser(BanDTO banDTO, UUID id);
}
