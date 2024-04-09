package com.blue.foxbuy.models.DTOs;

import com.blue.foxbuy.models.Ad;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
public class AdResultDTO {
    private UUID id;
    private String title;
    private String description;
    private double price;
    private int zipcode;
    private int categoryID;

    public AdResultDTO(Ad ad) {
        this.id = ad.getId();
        this.title = ad.getTitle();
        this.description = ad.getDescription();
        this.price = ad.getPrice();
        this.zipcode = ad.getZipcode();
        this.categoryID = ad.getCategoryID();
    }
}
