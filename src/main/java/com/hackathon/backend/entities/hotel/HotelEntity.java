package com.hackathon.backend.entities.hotel;

import com.hackathon.backend.entities.country.CountryEntity;
import com.hackathon.backend.entities.country.PlaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hotels")
@Getter
@Setter
@NoArgsConstructor
public class HotelEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String hotelName;
    private String mainImage;
    private String address;
    private String title;
    private String description;
    private int hotelRoomsCount;
    private float rate = 0f;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "country_id")
    private CountryEntity country;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="place_id")
    private PlaceEntity place;

    @OneToOne(mappedBy = "hotel", fetch = FetchType.EAGER)
    private RoomDetailsEntity roomDetails;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<RoomEntity> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<HotelEvaluationEntity> evaluations = new ArrayList<>();;

    public HotelEntity(String hotelName, String mainImage,
                       String title, String description,
                       int hotelRoomsCount, String address,
                       CountryEntity country, PlaceEntity foundPlace) {
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.title = title;
        this.description = description;
        this.hotelRoomsCount = hotelRoomsCount;
        this.address = address;
        this.country = country;
        this.place = foundPlace;
    }
}