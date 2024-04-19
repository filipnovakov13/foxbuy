package com.blue.foxbuy.services.implementations;

import com.blue.foxbuy.models.Log;
import com.blue.foxbuy.repositories.LogRepository;
import com.blue.foxbuy.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Autowired
    public LogServiceImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }


    @Override
    public List<Log> getLogsByDate(LocalDate date) {
        return logRepository.findByTimestamp(date);
    }

    @Override
    public List<Log> getAllLogs() {
        return logRepository.findAll();
    }
}
