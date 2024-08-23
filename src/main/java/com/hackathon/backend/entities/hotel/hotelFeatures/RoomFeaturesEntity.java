package com.hackathon.backend.entities.hotel.hotelFeatures;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hackathon.backend.entities.hotel.RoomDetailsEntity;
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
@Table(name = "roomFeatures")
public class RoomFeaturesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String roomFeatures;

    @JsonIgnore
    @ManyToMany(mappedBy = "roomFeatures", fetch = FetchType.LAZY)
    private List<RoomDetailsEntity> roomDetails = new ArrayList<>();

    public RoomFeaturesEntity(String roomFeature) {
        this.roomFeatures = roomFeature;
    }
}
