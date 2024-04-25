package com.blue.foxbuy.models;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Hidden
public class AdCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(unique = true)
    private String name;

    private String description;

    @OneToMany(mappedBy = "adCategory", cascade = CascadeType.ALL)
    @ToString.Exclude
    private List<Ad> ads;

    public AdCategory(String name, String description) {
        this.name = name;
        this.description = description;
        this.ads = new ArrayList<>();
    }
}
