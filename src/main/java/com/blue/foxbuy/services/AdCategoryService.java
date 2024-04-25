package com.blue.foxbuy.services;

import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.DTOs.AdCategoryDTO;

import java.util.List;

public interface AdCategoryService {

    boolean doesCategoryExist(String name);
    boolean doesCategoryExist(Integer id);
    AdCategory findCategoryById(Integer id);
    AdCategory findCategoryByName(String name);
    AdCategory saveAdCategoryDTO(AdCategoryDTO adCategoryDTO);
    AdCategory updateCategory(Integer id, AdCategoryDTO adCategoryDTO);
    void deleteById(Integer id);
    List<AdCategory> findAllCategoriesWithAds();
    List<AdCategory> findAll();
}