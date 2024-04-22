package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.Ad;
import com.blue.foxbuy.models.User;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdRepository extends JpaRepository<Ad, UUID> {

    @Query(value = "SELECT * FROM ad WHERE owner_id IN (SELECT id FROM user WHERE username = ?1)", nativeQuery = true)
    List<Ad> findAllByUser(String username);

//    @Query(value = "SELECT * FROM ad WHERE category_id = ?1 ORDER BY creation_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
//    List<Ad> findAllByCategoryId(int i, int pageSize, int page);

    @Query(value = "SELECT * FROM ad WHERE category_id = :categoryId ORDER BY creation_date DESC",
            countQuery = "SELECT count(*) FROM ad WHERE category_id = :categoryId",
            nativeQuery = true)
    Page<Ad> findAllByCategoryId(int categoryId, Pageable pageable);
}
