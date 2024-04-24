package com.blue.foxbuy.models.DTOs;

import com.blue.foxbuy.models.Ad;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOfAdsDTO {

    private int page;

    @Column(name = "total_pages")
    private int totalPages;

    private List<AdDTO> ads = new ArrayList<>();

    public void add(Ad ad) {
        AdDTO adDTO = new AdDTO(
                ad.getTitle(),
                ad.getDescription(),
                ad.getPrice(),
                ad.getZipcode(),
                ad.getAdCategory().getId()
        );
        this.ads.add(adDTO);
    }
}
