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
    private Double price;
    private String zipcode;
    @ManyToOne
    private User owner;
    @ManyToOne
    private AdCategory adCategory;
    private boolean visible;

    public Ad(String title, String description, Double price, String zipcode, User owner) {
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
        this.price = price;
        this.zipcode = zipcode;
        this.owner = owner;
        setVisible(true);
    }
}