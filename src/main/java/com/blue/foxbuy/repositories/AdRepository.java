package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

public interface AdRepository extends JpaRepository<Ad, UUID> {

    Page<Ad> findAllByCategoryIDOrderById(int categoryId, Pageable pageable);
    // List<Ad> findAllByUserId(UUID userId);
    long count();
}
