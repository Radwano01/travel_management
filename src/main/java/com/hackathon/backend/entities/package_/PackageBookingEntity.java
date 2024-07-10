package com.hackathon.backend.entities.package_;

import com.hackathon.backend.entities.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "packageBooking")
@Getter
@Setter
@NoArgsConstructor
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

    public PackageBookingEntity(UserEntity user, PackageEntity package_){
        this.user = user;
        this.package_ = package_;
    }
}
