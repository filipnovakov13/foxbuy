package com.blue.foxbuy.models;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Object used for storing categories of ads")
public class AdCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Schema(example = "1")
    private Integer id;
    @Column(unique = true)
    @Schema(description = "Name must be unique", example = "IT")
    private String name;
    @Schema(example = "Technical gizmos and gadgets")
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
