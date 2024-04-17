package com.blue.foxbuy.repositories;

import com.blue.foxbuy.models.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
@Repository
public interface LogRepository extends JpaRepository<Log, Long> {
    List<Log> findByTimestamp(LocalDate date);
}
