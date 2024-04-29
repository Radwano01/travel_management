package com.hackathon.backend.entities.hotel;

import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "hotelEvaluations")
@Getter
@Setter
@NoArgsConstructor
public class HotelEvaluationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String comment;
    private float rate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "hotel_id")
    private HotelEntity hotel;

    public HotelEvaluationEntity(String comment, float rate,
                                 HotelEntity hotel, UserEntity user) {
        this.comment = comment;
        this.rate = rate;
        this.hotel = hotel;
        this.user = user;
    }
}
