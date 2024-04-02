package com.blue.foxbuy.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    private String description;
    private Date creationDate;
    private int price;
    private int zipcode;

    public Ad(UUID id, String title, String description, Date creationDate, int price, int zipcode) {
        this.id = UUID.randomUUID();
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
        this.price = price;
        this.zipcode = zipcode;
    }
}
