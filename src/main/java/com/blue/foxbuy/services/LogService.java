package com.blue.foxbuy.services;

import com.blue.foxbuy.models.Log;

import java.time.LocalDate;
import java.util.List;

public interface LogService {

    List<Log> getLogsByDate(LocalDate date);

    List<Log> getAllLogs();
}
