package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.ListOfAdsDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.services.AdService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class AdServiceImp implements AdService {
    private final AdRepository adRepository;

    @Autowired
    public AdServiceImp(AdRepository adRepository) {
        this.adRepository = adRepository;
    }

    @Override
    public Ad saveAdDTO(AdDTO adDTO, User owner) {
        Ad ad = new Ad(adDTO.getTitle(), adDTO.getDescription(), adDTO.getPrice(), adDTO.getZipcode(), owner);
        return adRepository.save(ad);
    }

    @Override
    public Ad saveAd(Ad ad) {
        return adRepository.save(ad);
    }

    @Override
    public boolean canUserCreateAd(User user) {
        return user.getAds().size() < 3;
    }

    @Override
    public AdDTO getAdById(UUID id) {
        Ad ad = adRepository.findById(id).orElseThrow(() -> new RuntimeException("Ad not found based on ID."));
        return new AdDTO(ad.getTitle(), ad.getDescription(), ad.getPrice(), ad.getZipcode(), ad.getAdCategory().getId());
    }

    @Override
    public ListOfAdsDTO getAdsByUser(String username) {
        List<Ad> ads = adRepository.findAllByOwner_Username(username);
        ListOfAdsDTO list = new ListOfAdsDTO();
        for (Ad ad : ads){
            list.add(ad);
        }
        return list;
    }

    @Override
    public ListOfAdsDTO getAdsByCategory(int categoryId, int page) {
        PageRequest pr = PageRequest.of(page, 10);
        Page<Ad> tmp = adRepository.findAllByAdCategory_Id(categoryId,pr);
        ListOfAdsDTO ads = new ListOfAdsDTO(tmp.getPageable().getPageNumber(), tmp.getTotalPages(), null);
        for (Ad ad : tmp.getContent()){
            ads.add(ad);
        }
        return ads;
    }
}
