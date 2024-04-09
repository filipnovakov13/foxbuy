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
    private double price;
    private int zipcode;
    private int categoryID;

    public Ad(String title, String description, double price, int zipcode) {
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
        this.price = price;
        this.zipcode = zipcode;
    }
}