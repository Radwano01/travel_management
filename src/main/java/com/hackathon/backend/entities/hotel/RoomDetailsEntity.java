package com.hackathon.backend.entities.hotel;

import com.hackathon.backend.entities.hotel.hotelFeatures.HotelFeaturesEntity;
import com.hackathon.backend.entities.hotel.hotelFeatures.RoomFeaturesEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "roomDetails")
@Getter
@Setter
@NoArgsConstructor
public class RoomDetailsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String imageOne;
    private String imageTwo;
    private String imageThree;
    private String imageFour;

    @Column(length = 1000)
    private String description;

    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "hotelFeaturesRelation",
            joinColumns = @JoinColumn(name = "room_details_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id", referencedColumnName = "id"))
    private List<HotelFeaturesEntity> hotelFeatures = new ArrayList<>();

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "roomFeaturesRelation",
            joinColumns = @JoinColumn(name = "room_details_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id", referencedColumnName = "id"))
    private List<RoomFeaturesEntity> roomFeatures = new ArrayList<>();;

    public RoomDetailsEntity(String imageOne, String imageTwo,
                             String imageThree, String imageFour,
                             String description, int price,
                             HotelEntity hotel) {
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.imageFour = imageFour;
        this.description = description;
        this.price = price;
        this.hotel = hotel;
    }

    public RoomDetailsEntity(String imageOne, String imageTwo,
                             String imageThree, String imageFour,
                             String description, int price) {
        this.imageOne = imageOne;
        this.imageTwo = imageTwo;
        this.imageThree = imageThree;
        this.imageFour = imageFour;
        this.description = description;
        this.price = price;
    }
}
