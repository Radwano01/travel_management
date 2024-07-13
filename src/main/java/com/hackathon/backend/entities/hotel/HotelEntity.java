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

    @Column(length = 100)
    private String description;

    private int hotelRoomsCount;

    private int rate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private PlaceEntity place;


    @OneToOne(mappedBy = "hotel", fetch = FetchType.EAGER)
    private RoomDetailsEntity roomDetails;

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<RoomEntity> rooms = new ArrayList<>();

    @OneToMany(mappedBy = "hotel", fetch = FetchType.LAZY)
    private List<HotelEvaluationEntity> evaluations = new ArrayList<>();;

    public HotelEntity(String hotelName, String mainImage,
                       String description, int hotelRoomsCount,
                       String address, int rate,
                       PlaceEntity place) {
        this.hotelName = hotelName;
        this.mainImage = mainImage;
        this.description = description;
        this.hotelRoomsCount = hotelRoomsCount;
        this.address = address;
        this.rate = rate;
        this.place = place;

    }
}
