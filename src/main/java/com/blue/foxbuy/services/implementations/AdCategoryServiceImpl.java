package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.AdCategory;
import com.blue.foxbuy.models.DTOs.AdCategoryDTO;
import com.blue.foxbuy.repositories.AdCategoryRepository;
import com.blue.foxbuy.services.AdCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdCategoryServiceImpl implements AdCategoryService {

    private final AdCategoryRepository adCategoryRepository;

    @Autowired
    public AdCategoryServiceImpl(AdCategoryRepository adCategoryRepository) {
        this.adCategoryRepository = adCategoryRepository;
    }

    @Override
    public boolean doesCategoryExist(String name) {
        return adCategoryRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean doesCategoryExist(Integer id) {
        return adCategoryRepository.existsById(id);
    }

    @Override
    public AdCategory saveAdCategoryDTO(AdCategoryDTO adCategoryDTO) {
        AdCategory createdCategory = new AdCategory(adCategoryDTO.getName(), adCategoryDTO.getDescription());

        return adCategoryRepository.save(createdCategory);
    }

    @Override
    public AdCategory findCategoryById(Integer id) {
        return adCategoryRepository.findAdCategoryById(id);
    }

    @Override
    public AdCategory updateCategory(Integer id, AdCategoryDTO adCategoryDTO) {
        AdCategory updatedCategory = adCategoryRepository.findAdCategoryById(id);
        updatedCategory.setName(adCategoryDTO.getName());
        updatedCategory.setDescription(adCategoryDTO.getDescription());
        return adCategoryRepository.save(updatedCategory);
    }

    @Override
    public void deleteById(Integer id) {
        adCategoryRepository.deleteById(id);
    }

    @Override
    public List<AdCategory> findAllCategoriesWithAds() {
        return adCategoryRepository.findAllCategoriesWithAds();
    }

    @Override
    public List<AdCategory> findAll() {
        return adCategoryRepository.findAll();
    }

    @Override
    public AdCategory findCategoryByName(String name) {
        return adCategoryRepository.findAdCategoryByNameIgnoreCase(name);
    }
}
