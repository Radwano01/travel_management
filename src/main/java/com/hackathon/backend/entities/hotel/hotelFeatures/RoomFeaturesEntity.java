package com.hackathon.backend.entities.hotel.hotelFeatures;

import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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

    @ManyToMany
    private List<RoomDetailsEntity> roomDetails = new ArrayList<>();

    public RoomFeaturesEntity(String roomFeature) {
        this.roomFeatures = roomFeature;
    }
}
