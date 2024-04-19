package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.AdCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdCategoryRepository extends JpaRepository<AdCategory, Integer> {

    boolean existsByNameIgnoreCase(String name);

    boolean existsById(Integer id);
    AdCategory findAdCategoryById(Integer id);

    AdCategory findAdCategoryByNameIgnoreCase(String name);
}