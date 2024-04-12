package com.blue.foxbuy.models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdDTO {

    private String title;
    private String description;
    private Date creationDate;
    private double price;
    private int zipcode;
    private int categoryID;

}