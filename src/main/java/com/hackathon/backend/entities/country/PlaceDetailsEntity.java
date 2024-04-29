package com.hackathon.backend.entities.country;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="placeDetails")
@Getter
@Setter
@NoArgsConstructor
public class PlaceDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String description;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "place_id")
    private PlaceEntity place;

    public PlaceDetailsEntity(String imageOne, String imageTwo,
                              String imageThree, String description,
                              PlaceEntity place) {
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.description = description;
        this.place = place;
    }
}
