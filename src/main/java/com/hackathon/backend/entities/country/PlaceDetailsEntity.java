package com.hackathon.backend.entities.country;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="placeDetails")
public class PlaceDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String imageOne;
    private String imageTwo;
    private String imageThree;

    @Column(length = 2000)
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

    @Override
    public String toString() {
        return "PlaceDetailsEntity{" +
                "id=" + id +
                ", imageOne='" + imageOne + '\'' +
                ", imageTwo='" + imageTwo + '\'' +
                ", imageThree='" + imageThree + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
