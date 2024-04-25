package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.AdCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdCategoryRepository extends JpaRepository<AdCategory, Integer> {

    boolean existsByNameIgnoreCase(String name);
    boolean existsById(Integer id);
    AdCategory findAdCategoryById(Integer id);
    AdCategory findAdCategoryByNameIgnoreCase(String name);
    @Query("SELECT c FROM AdCategory c WHERE SIZE(c.ads) > 0")
    List<AdCategory> findAllCategoriesWithAds();
    void save(String name);
}