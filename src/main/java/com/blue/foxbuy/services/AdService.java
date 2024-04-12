package com.blue.foxbuy.services;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.ListOfAdsDTO;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.UUID;

public interface AdService {

    AdDTO getAdById(UUID id);
    ListOfAdsDTO getAdsByCategory(int categoryId, int page);
    List<AdDTO> getAdsByUser(String username);
}
