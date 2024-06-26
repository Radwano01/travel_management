package com.hackathon.backend.entities.hotel.hotelFeatures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @JsonIgnore
    @ManyToMany(mappedBy = "hotelFeatures", fetch = FetchType.LAZY)
    private List<RoomDetailsEntity> roomDetails = new ArrayList<>();

    public HotelFeaturesEntity(String hotelFeature) {
        this.hotelFeatures = hotelFeature;
    }
}
