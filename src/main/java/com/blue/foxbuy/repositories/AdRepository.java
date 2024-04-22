package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.Ad;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;

@Repository
public interface AdRepository extends JpaRepository<Ad, UUID> {

    List<Ad> findAllByOwner_Username(String username);

//    @Query(value = "SELECT * FROM ad WHERE category_id = ?1 ORDER BY creation_date DESC LIMIT ?2 OFFSET ?3", nativeQuery = true)
//    List<Ad> findAllByCategoryId(int i, int pageSize, int page);

    Page<Ad> findAllByAdCategory_Id(int categoryId, Pageable pageable);
}
