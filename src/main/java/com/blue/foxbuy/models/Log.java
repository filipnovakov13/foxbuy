package com.blue.foxbuy.models;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Entity
@Data
@NoArgsConstructor
@Hidden
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private LocalDateTime timestamp;
    private String endpoint;
    private String type;
    private String data;

    public Log(String endpoint, String type, String data) {
        this.timestamp = LocalDateTime.now();
        this.endpoint = endpoint;
        this.type = type;
        this.data = data;
    }
}
