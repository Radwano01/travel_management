package com.hackathon.backend.entities.hotel.hotelFeatures;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "roomFeatures")
@Getter
@Setter
@NoArgsConstructor
public class RoomFeaturesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roomFeatures;

    public RoomFeaturesEntity(String roomFeature) {
        this.roomFeatures = roomFeature;
    }
}
