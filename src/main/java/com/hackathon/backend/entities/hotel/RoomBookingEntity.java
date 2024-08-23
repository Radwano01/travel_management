package com.hackathon.backend.entities.hotel;

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
@Table(name = "roomBooking")
public class RoomBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reservationName;
    private LocalDateTime bookedDate;

    public RoomBookingEntity(UserEntity user,
                             HotelEntity hotel,
                             LocalDateTime startTime,
                             LocalDateTime endTime,
                             String reservationName,
                             LocalDateTime bookedDate) {
        this.reservationName = reservationName;
        this.user = user;
        this.hotel = hotel;
        this.startTime = startTime;
        this.endTime = endTime;
        this.bookedDate = bookedDate;
    }
}
