package com.blue.foxbuy.services;

import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.DTOs.AdCategoryDTO;

public interface AdCategoryService {

    boolean doesCategoryExist(String name);
    boolean doesCategoryExist(Integer id);
    AdCategory findCategoryById(Integer id);
    AdCategory findCategoryByName(String name);
    AdCategory createCategory(AdCategoryDTO adCategoryDTO);
    AdCategory updateCategory(Integer id, AdCategoryDTO adCategoryDTO);
    void deleteById(Integer id);
}
