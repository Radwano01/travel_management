package com.hackathon.backend.entities.plane;

import com.hackathon.backend.entities.country.CountryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "flights")
@Getter
@Setter
@NoArgsConstructor
public class PlaneFlightsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int price;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plane_id")
    private PlaneEntity plane;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "departure_id")
    private CountryEntity departureCountry;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "destination_id")
    private CountryEntity destinationCountry;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;


    public PlaneFlightsEntity(int price, PlaneEntity plane,
                              CountryEntity departureCountry,
                              CountryEntity destinationCountry,
                              LocalDateTime departureTime,
                              LocalDateTime arrivalTime) {
        this.price = price;
        this.plane = plane;
        this.departureCountry = departureCountry;
        this.destinationCountry = destinationCountry;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }
}
