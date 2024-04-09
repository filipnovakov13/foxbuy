package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.Ad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdRepository extends JpaRepository<Ad, UUID> {
}
