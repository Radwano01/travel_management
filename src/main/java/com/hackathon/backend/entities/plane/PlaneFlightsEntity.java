package com.hackathon.backend.entities.plane;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "flights")
public class PlaneFlightsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private int price;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plane_id")
    private PlaneEntity plane;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_id")
    private AirPortEntity departureAirPort;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id")
    private AirPortEntity destinationAirPort;

    private LocalDateTime departureTime;

    private LocalDateTime arrivalTime;


    public PlaneFlightsEntity(int price, PlaneEntity plane,
                              AirPortEntity departureAirPort,
                              AirPortEntity destinationAirPort,
                              LocalDateTime departureTime,
                              LocalDateTime arrivalTime) {
        this.price = price;
        this.plane = plane;
        this.departureAirPort = departureAirPort;
        this.destinationAirPort = destinationAirPort;
        this.departureTime = departureTime;
        this.arrivalTime = arrivalTime;
    }

    @Override
    public String toString() {
        return "PlaneFlightsEntity{" +
                "id=" + id +
                ", price=" + price +
                ", departureAirPort=" + departureAirPort +
                ", destinationAirPort=" + destinationAirPort +
                ", departureTime=" + departureTime +
                ", arrivalTime=" + arrivalTime +
                '}';
    }
}