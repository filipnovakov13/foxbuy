package com.blue.foxbuy.services;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;

import java.util.UUID;

public interface AdService {
    Ad saveAdDTO(AdDTO adDTO, UUID owner);

    Ad saveAd(Ad ad);

    boolean canUserCreateAd(UUID userId);
}
