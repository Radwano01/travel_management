package com.hackathon.backend.entities.country;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "places")
@Getter
@Setter
@NoArgsConstructor
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String place;
    private String mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<HotelEntity> hotels;

    @OneToOne(mappedBy = "place", fetch = FetchType.LAZY)
    private PlaceDetailsEntity placeDetails;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY)
    private List<AirPortEntity> airPorts;

    public PlaceEntity(String place,
                       String mainImage,
                       CountryEntity country) {
        this.place = place;
        this.mainImage = mainImage;
        this.country = country;
    }

    public PlaceEntity(int id, String place,
                       String mainImage) {
        this.id = id;
        this.place = place;
        this.mainImage = mainImage;
    }
}
