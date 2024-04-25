package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.services.AdCategoryService;
import com.blue.foxbuy.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AdServiceImp implements AdService {
    private final AdRepository adRepository;
    private final AdCategoryService adCategoryService;

    @Autowired
    public AdServiceImp(AdRepository adRepository, AdCategoryService adCategoryService) {
        this.adRepository = adRepository;
        this.adCategoryService = adCategoryService;
    }

    @Override
    public Ad saveAdDTO(AdDTO adDTO, User owner) {
        if (adCategoryService.doesCategoryExist(adDTO.getCategoryID())) {
            Ad ad = new Ad(adDTO.getTitle(), adDTO.getDescription(), adDTO.getPrice(), adDTO.getZipcode(), owner, adCategoryService.findCategoryById(adDTO.getCategoryID()));
            return adRepository.save(ad);
        } else throw new NullPointerException("Oops. Something went wrong. Category doesn't exist.");
    }

    @Override
    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    @Override
    public boolean canUserCreateAd(User user) {
        return user.getAds().size() < 3;
    }
}
