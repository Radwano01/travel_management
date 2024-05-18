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
    private UserEntity users;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id", insertable = false, updatable = false)
    private RoomEntity rooms;


    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public RoomBookingEntity(UserEntity user, RoomEntity room,
                             LocalDateTime startTime,
                             LocalDateTime endTime) {
        this.users = user;
        this.rooms = room;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
