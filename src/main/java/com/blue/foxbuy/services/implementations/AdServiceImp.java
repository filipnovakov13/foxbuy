package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;
import com.blue.foxbuy.models.DTOs.ListOfAdsDTO;
import com.blue.foxbuy.models.User;
import com.blue.foxbuy.repositories.AdRepository;
import com.blue.foxbuy.services.AdService;
import com.blue.foxbuy.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdServiceImp implements AdService {

    private final AdRepository adRepository;
    private final UserService userService;


    @Override
    public AdDTO getAdById(final UUID id) {
        if (id == null){
            throw new RuntimeException("Ad ID is missing.");
        }
        Optional<Ad> tmp = adRepository.findById(id);
        Ad ad =  tmp.orElseThrow(() -> new RuntimeException("Ad not found."));

        return new AdDTO(
                ad.getTitle(),
                ad.getDescription(),
                ad.getCreationDate(),
                ad.getPrice(),
                ad.getZipcode(),
                ad.getCategoryID()
        );
    }

    @Override
    public ListOfAdsDTO getAdsByCategory(final int categoryId, int page) {
        if (page < 0){
            throw new RuntimeException("Page number cannot be negative value.");
        }

        if (page >= 1){ // paging starts from 0
            page -=1;
        }

        Pageable pageable = PageRequest.of(page, 10);
        Page<Ad> result = adRepository.findAllByCategoryIDOrderById(categoryId, pageable);
        if (result.isEmpty()) {
            throw new RuntimeException("Ads based on category not found.");
        }

        long totalAds = adRepository.count();
        int totalPages = (int) Math.ceil((double) totalAds / 10);

        ListOfAdsDTO listOfAdsDTO = new ListOfAdsDTO();
        listOfAdsDTO.setPage(page);
        listOfAdsDTO.setTotalPages(totalPages);
        for (Ad ad : result){
            listOfAdsDTO.add(ad);
        }
        return listOfAdsDTO;
    }

    @Override
    public List<AdDTO> getAdsByUser(String username) {
        User tmp = userService.findByUsername(username);
        if (tmp == null){
            throw new RuntimeException("User not found.");
        }


        List<Ad> result = tmp.getAdId();
        if (result.isEmpty()){
            throw new RuntimeException("User does not have any ads.");
        }

        List<AdDTO> listOfAds = new ArrayList<>();
        for (Ad ad : result){
            listOfAds.add(new AdDTO(
                    ad.getTitle(),
                    ad.getDescription(),
                    ad.getCreationDate(),
                    ad.getPrice(),
                    ad.getZipcode(),
                    ad.getCategoryID()
            ));
        }
        return listOfAds;
    }
}
