package com.blue.foxbuy.services;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.ListOfAdsDTO;
import com.blue.foxbuy.models.User;

import java.util.UUID;

public interface AdService {
    Ad saveAdDTO(AdDTO adDTO, User owner);

    Ad saveAd(Ad ad);

    boolean canUserCreateAd(User user);

    AdDTO getAdById(UUID id);

    ListOfAdsDTO getAdsByUser(String username);

    ListOfAdsDTO getAdsByCategory(int categoryId, int page);
}
