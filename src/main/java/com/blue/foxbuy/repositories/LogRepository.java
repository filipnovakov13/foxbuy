package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByTimestampBetween(LocalDateTime date1, LocalDateTime date2);
}
