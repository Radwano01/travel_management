package com.hackathon.backend.entities.package_;

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
@Table(name = "packageBooking")
public class PackageBookingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "package_id")
    private PackageEntity package_;

    private String reservationName;
    private LocalDateTime bookedDate;

    public PackageBookingEntity(UserEntity user,
                                PackageEntity package_,
                                String reservationName,
                                LocalDateTime bookedDate){
        this.user = user;
        this.package_ = package_;
        this.reservationName = reservationName;
        this.bookedDate = bookedDate;
    }
}
