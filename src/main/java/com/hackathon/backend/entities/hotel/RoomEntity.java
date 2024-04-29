package com.hackathon.backend.entities.hotel;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rooms")
@Getter
@Setter
@NoArgsConstructor
public class RoomEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private boolean status = false;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    public RoomEntity(HotelEntity hotel) {
        this.hotel = hotel;
    }
}