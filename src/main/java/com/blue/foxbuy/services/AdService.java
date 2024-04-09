package com.blue.foxbuy.services;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.DTOs.AdDTO;

public interface AdService {
    boolean isPriceValid(double price);
    boolean isZipcodeValid(int zipcode);
    Ad save(AdDTO adDTO);
}
