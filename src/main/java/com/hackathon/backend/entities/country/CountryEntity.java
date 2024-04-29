package com.hackathon.backend.entities.country;

import com.hackathon.backend.entities.hotel.HotelEntity;
import com.hackathon.backend.entities.plane.PlaneFlightsEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "countries")
@Getter
@Setter
@NoArgsConstructor
public class CountryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String country;

    private String mainImage;

    @OneToMany(mappedBy = "country",fetch = FetchType.LAZY)
    private List<HotelEntity> hotels = new ArrayList<>();

    @OneToOne(mappedBy ="country", fetch = FetchType.LAZY)
    private CountryDetailsEntity countryDetails;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<PlaceEntity> places = new ArrayList<>();

    @OneToMany(mappedBy = "departureCountry", fetch = FetchType.LAZY)
    private List<PlaneFlightsEntity> departingFlights = new ArrayList<>();

    @OneToMany(mappedBy = "destinationCountry", fetch = FetchType.LAZY)
    private List<PlaneFlightsEntity> arrivingFlights = new ArrayList<>();

    public CountryEntity(String country,
                         String mainImage){
        this.country = country;
        this.mainImage = mainImage;
    }

    public CountryEntity(String country) {
        this.country = country;
    }
}