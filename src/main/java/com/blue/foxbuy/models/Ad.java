package com.blue.foxbuy.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
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
//    @NotBlank(message = "Set a title, please.")
    private String title;
//    @NotBlank(message = "Include a description, please.")
    private String description;
    private Date creationDate;
//    @NotBlank(message = "Set a price, please.")
    private double price;
//    @NotBlank(message = "Include your zipcode, please.")
    private int zipcode;
    private int categoryID;
    @ManyToOne
    private User owner;
    private boolean visible;

    public Ad(String title, String description, double price, int zipcode, User owner) {
        this.title = title;
        this.description = description;
        this.creationDate = new Date();
        this.price = price;
        this.zipcode = zipcode;
        this.owner = owner;
        setVisible(true);
    }
}