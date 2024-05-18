package com.hackathon.backend.entities.plane;

import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "visaBooking")
@Getter
@Setter
@NoArgsConstructor
public class PlaneSeatsBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    private UserEntity users;


    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "visa_id", insertable = false, updatable = false)
    private PlaneSeatsEntity planeSeats;



    public PlaneSeatsBookingEntity(UserEntity user,
                             PlaneSeatsEntity planeSeats
                             ){
        this.users = user;
        this.planeSeats = planeSeats;
    }

}
