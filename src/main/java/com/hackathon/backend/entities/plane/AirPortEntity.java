package com.hackathon.backend.entities.plane;

import com.hackathon.backend.entities.country.PlaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "airports")
public class AirPortEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String airPortName;

    private String airPortCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private PlaceEntity place;

    @OneToMany(mappedBy = "departureAirPort", fetch = FetchType.LAZY)
    private List<PlaneFlightsEntity> departureFlights;

    @OneToMany(mappedBy = "destinationAirPort", fetch = FetchType.LAZY)
    private List<PlaneFlightsEntity> destinationFlights;

    public AirPortEntity(String airPortName,
                         String airPortCode,
                         PlaceEntity place) {
        this.airPortName = airPortName;
        this.airPortCode = airPortCode;
        this.place = place;
    }

    @Override
    public String toString() {
        return "AirPortEntity{" +
                "id=" + id +
                ", airPortName='" + airPortName + '\'' +
                ", airPortCode='" + airPortCode + '\'' +
                '}';
    }
}

