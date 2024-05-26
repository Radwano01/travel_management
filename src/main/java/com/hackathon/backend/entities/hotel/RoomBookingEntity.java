package com.hackathon.backend.entities.hotel;

import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(name = "roomBooking")
@Getter
@Setter
@NoArgsConstructor
public class RoomBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id", insertable = false, updatable = false)
    private HotelEntity hotel;


    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public RoomBookingEntity(UserEntity user, HotelEntity hotel,
                             LocalDateTime startTime,
                             LocalDateTime endTime) {
        this.user = user;
        this.hotel = hotel;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
