package com.blue.foxbuy.models;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class Ad {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String title;
    @Column(length = 3190)
    private String description;
    private Date creationDate;
    private Double price;
    private String zipcode;
    @ManyToOne
    private User owner;
    @ManyToOne
    private AdCategory adCategory;
    private boolean visible;

    public Ad(String title, String description, Double price, String zipcode, User owner, AdCategory adCategory) {
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
        this.price = price;
        this.zipcode = zipcode;
        this.owner = owner;
        this.adCategory = adCategory;
        setVisible(true);
    }
}