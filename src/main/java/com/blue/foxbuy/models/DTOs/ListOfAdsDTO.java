package com.blue.foxbuy.models.DTOs;

import com.blue.foxbuy.models.Ad;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListOfAdsDTO {

    private int page;

    @Column(name = "total_pages")
    private int totalPages;

    private List<AdDTO> ads;

    public void add(Ad ad) {
        AdDTO adDTO = new AdDTO(
                ad.getTitle(),
                ad.getDescription(),
                ad.getCreationDate(),
                ad.getPrice(),
                ad.getZipcode(),
                ad.getCategoryID()
        );
        this.ads.add(adDTO);
    }
}
