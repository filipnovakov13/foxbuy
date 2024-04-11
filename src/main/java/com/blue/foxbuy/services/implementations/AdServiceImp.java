package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdServiceImp implements AdService {
    private final AdRepository adRepository;

    @Autowired
    public AdServiceImp(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @Override
    public Ad saveAdDTO(AdDTO adDTO, UUID owner) {
        Ad ad = new Ad(adDTO.getTitle(), adDTO.getDescription(), adDTO.getPrice(), adDTO.getZipcode(), owner);
        return adRepository.save(ad);
    }

    @Override
    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    @Override
    public boolean canUserCreateAd(UUID userId) {
        long adCount = adRepository.countByOwner(userId);
        return adCount < 3;
    }
}
