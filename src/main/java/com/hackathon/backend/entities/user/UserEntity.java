package com.hackathon.backend.entities.user;


import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="users")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String username;

    private String email;
    private String password;
    private boolean verificationStatus = false;
    private String image;

    //details
    private String fullName;
    private String country;
    private String phoneNumber;
    private String address;
    private LocalDate dateOfBirth;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<HotelEvaluationEntity> hotelEvaluations = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private List<PackageEvaluationEntity> packageEvaluations = new ArrayList<>();

    public UserEntity(String username, String email,
                      String password, String image,
                      String fullName, String country,
                      String phoneNumber, String address,
                      LocalDate dateOfBirth, RoleEntity role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.image = image;
        this.fullName = fullName;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", verificationStatus=" + verificationStatus +
                ", image='" + image + '\'' +
                ", fullName='" + fullName + '\'' +
                ", country='" + country + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", role=" + role +
                '}';
    }
}
