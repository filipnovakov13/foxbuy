package com.blue.foxbuy.controllers;

import com.blue.foxbuy.models.Log;
import com.blue.foxbuy.services.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class LogController {

    private final LogService logService;

    @Autowired
    public LogController(LogService logService) {
        this.logService = logService;
    }

    @GetMapping("/logs")
    public ResponseEntity<List<Log>> getLogs(@RequestParam(name = "date", required = false)
                                 @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date==null) {
            return ResponseEntity.ok().body(logService.getAllLogs());
        } else
            return ResponseEntity.ok().body(logService.getLogsByDate(date));
    }
}
