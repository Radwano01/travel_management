package com.hackathon.backend.entities.hotel.hotelFeatures;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hotelFeatures")
@Getter
@Setter
@NoArgsConstructor
public class HotelFeaturesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String hotelFeatures;

    public HotelFeaturesEntity(String hotelFeature) {
        this.hotelFeatures = hotelFeature;
    }
}
