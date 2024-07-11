package com.hackathon.backend.entities.plane;


import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "planeSeats")
public class PlaneSeatsEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private char seatClass;
    private int seatNumber;
    private boolean status = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plane_id")
    private PlaneEntity plane;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    public PlaneSeatsEntity(Character seatClass, int seatNumber, PlaneEntity plane) {
        this.seatClass = seatClass;
        this.seatNumber = seatNumber;
        this.plane = plane;
    }
}
