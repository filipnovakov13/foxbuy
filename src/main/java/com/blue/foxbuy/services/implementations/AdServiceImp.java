package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdServiceImp implements AdService {
    private final AdRepository adRepository;

    @Autowired
    public AdServiceImp(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @Override
    public boolean isPriceValid(double price) {

        return false;
    }

    @Override
    public boolean isZipcodeValid(int zipcode) {
        return false;
    }

    @Override
    public Ad saveAdDTO(AdDTO adDTO) {
            Ad ad = new Ad(adDTO.getTitle(), adDTO.getDescription(), adDTO.getPrice(), adDTO.getZipcode());
            return adRepository.save(ad);
    }

    @Override
    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }
}
