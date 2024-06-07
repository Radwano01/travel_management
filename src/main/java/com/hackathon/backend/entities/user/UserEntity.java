package com.hackathon.backend.entities.user;


import com.hackathon.backend.entities.hotel.HotelEvaluationEntity;
import com.hackathon.backend.entities.package_.PackageEvaluationEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "username")
    private String username;

    private String email;
    private String password;
    private boolean verificationStatus = false;

    private String image;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="role_id")
    private RoleEntity role;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<HotelEvaluationEntity> evaluations = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    private List<PackageEvaluationEntity> packageEvaluations = new ArrayList<>();

    public UserEntity(String username, String email,
                      String password, RoleEntity role,
                      String image) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.image = image;
    }
}
