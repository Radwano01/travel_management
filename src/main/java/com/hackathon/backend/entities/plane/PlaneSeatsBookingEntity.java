package com.hackathon.backend.entities.plane;

import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "planeSeatsBooking")
@Getter
@Setter
@NoArgsConstructor
public class PlaneSeatsBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "plane_seat_id")
    private PlaneSeatsEntity planeSeat;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "flight_id")
    private PlaneFlightsEntity flight;

    private String reservationName;
    private LocalDateTime bookedDate;

    public PlaneSeatsBookingEntity(UserEntity user,
                                   PlaneSeatsEntity planeSeat,
                                   PlaneFlightsEntity flight,
                                   String reservationName,
                                   LocalDateTime bookedDate) {
        this.user = user;
        this.planeSeat = planeSeat;
        this.flight = flight;
        this.reservationName = reservationName;
        this.bookedDate = bookedDate;
    }
}
