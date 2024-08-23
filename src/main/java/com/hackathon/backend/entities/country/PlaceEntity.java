package com.hackathon.backend.entities.country;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.plane.AirPortEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "places")
public class PlaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String place;
    private String mainImage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<HotelEntity> hotels = new ArrayList<>();

    @OneToOne(mappedBy = "place", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private PlaceDetailsEntity placeDetails;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    private List<AirPortEntity> airPorts = new ArrayList<>();

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

    @Override
    public String toString() {
        return "PlaceEntity{" +
                "id=" + id +
                ", place='" + place + '\'' +
                ", mainImage='" + mainImage + '\'' +
                ", placeDetails=" + placeDetails +
                '}';
    }
}
