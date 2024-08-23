package com.hackathon.backend.entities.plane;

import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planeSeatsBooking")
public class PlaneSeatsBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private PlaneFlightsEntity flight;

    private String reservationName;
    private LocalDateTime bookedDate;

    public PlaneSeatsBookingEntity(UserEntity user,
                                   PlaneFlightsEntity flight,
                                   String reservationName,
                                   LocalDateTime bookedDate) {
        this.user = user;
        this.flight = flight;
        this.reservationName = reservationName;
        this.bookedDate = bookedDate;
    }
}
