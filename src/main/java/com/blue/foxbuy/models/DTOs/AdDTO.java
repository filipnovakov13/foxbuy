package com.blue.foxbuy.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {
    private String title;
    private String description;
    private double price;
    private int zipcode;
    private int categoryID;
}
